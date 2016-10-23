package com.nostalgi.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nostalgi.engine.IO.Net.INetworkLayer;
import com.nostalgi.engine.IO.Net.NetworkRole;
import com.nostalgi.engine.interfaces.IController;
import com.nostalgi.engine.interfaces.IGameEngine;
import com.nostalgi.engine.interfaces.IGameMode;
import com.nostalgi.engine.interfaces.World.IActor;
import com.nostalgi.engine.interfaces.World.ICharacter;
import com.nostalgi.engine.interfaces.World.IWorld;
import com.nostalgi.engine.physics.BoundingVolume;
import com.nostalgi.engine.physics.CollisionCategories;
import com.nostalgi.engine.Render.NostalgiCamera;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ksdkrol on 2016-07-04.
 */
public class NostalgiBaseEngine implements IGameEngine {
    private IGameMode mode;

    private NostalgiCamera currentCamera;
    private NostalgiRenderer mapRenderer;
    private InputMultiplexer inputProcessor;

    private INetworkLayer networkLayer;

    private IWorld world;

    private Box2DDebugRenderer debug;
    private HashMap<Integer, Short> floorMap = new HashMap<Integer, Short>();

    public NostalgiBaseEngine(IWorld world, NostalgiCamera camera, NostalgiRenderer mapRenderer, IGameMode mode) {
        this.world = world;
        this.currentCamera = camera;
        this.mode = mode;
        this.mapRenderer = mapRenderer;
        this.debug = new Box2DDebugRenderer();
    }

    @Override
    public void init() {
        // init input
        this.initInput();

        // Update playerbounds
        world.createBody(this.getGameMode().getCurrentController().getCurrentPossessedCharacter());
        //initPlayerBounds(this.getGameMode().getCurrentController().getCurrentPossessedCharacter());

        // Update terrain / map bounds
        initMapWalls();

        // Init map objects. like triggers, chests doors.
        initMapActors();
    }

    @Override
    public void update() {
        float dTime = Gdx.graphics.getDeltaTime();

        // Update game mode.
        ICharacter currentCharacter = getGameMode().getCurrentController().getCurrentPossessedCharacter();

        if(this.getGameMode().getGameState().getNetworkRole() == NetworkRole.ROLE_AUTHORITY) {

            this.getGameMode().update(dTime);
            for(IController controller :  this.getGameMode().getControllers()) {
                ICharacter character = controller.getCurrentPossessedCharacter();

                Body playerBody = character.getPhysicsBody();

                controller.update(dTime);

                playerBody.setLinearVelocity(character .getVelocity());

                if (character.fixtureNeedsUpdate()) {
                    world.updateBody(character);
                }

                // Update NPC / Monster bounds
                if (character.canEverTick()) {
                    character.tick(Gdx.graphics.getDeltaTime());
                }
            }

            tickActors(this.getGameMode().getGameState().getCurrentLevel().getActors(), dTime);

            world.step(1f / 60f, 6, 2);

            for(IController controller : this.getGameMode().getControllers()) {
                ICharacter character = controller.getCurrentPossessedCharacter();

                Body playerBody = character.getPhysicsBody();

                Vector2 playerPos = character.getWorldPosition();
                playerPos.x = playerBody.getPosition().x - 0.5f;
                playerPos.y = playerBody.getPosition().y - 0.5f;

                // replicate player state.
            }

            replicateActors(this.getGameMode().getGameState().getCurrentLevel().getActors());
        }
        else {
            Body playerBody = currentCharacter.getPhysicsBody();
            // put input into simulation
            getGameMode().getCurrentController().update(dTime);

            playerBody.setLinearVelocity(currentCharacter.getVelocity());

            // Send input to server.

            // Run simulation
            world.step(1f / 60f, 6, 2);

            Vector2 playerPos = currentCharacter.getWorldPosition();
            playerPos.x = playerBody.getPosition().x - 0.5f;
            playerPos.y = playerBody.getPosition().y - 0.5f;
        }

        // Update camera
        if(this.currentCamera != null) {
            this.currentCamera.setPositionSafe(currentCharacter.getWorldPosition());
        }

        this.currentCamera.update();

        // Set view
        this.mapRenderer.setView(this.currentCamera);
    }

    @Override
    public void render() {

        this.mapRenderer.setCurrentPlayerCharacter(getGameMode().getCurrentController().getCurrentPossessedCharacter());
        this.mapRenderer.render(Gdx.graphics.getDeltaTime());

        if(this.getGameMode().getHud() != null) {
            this.getGameMode().getHud().draw(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        }

        debug.render(world.getPhysicsWorld(), currentCamera.combined);
    }

    @Override
    public void dispose() {
        this.getGameMode().dispose();
        this.world.dispose();
    }

    @Override
    public IGameMode getGameMode() {
        return this.mode;
    }

    @Override
    public IWorld getWorld() {
        return this.world;
    }

    @Override
    public void setCurrentCamera(NostalgiCamera camera) {
        this.currentCamera = camera;
    }

    @Override
    public NostalgiCamera getCurrentCamera() {
        return this.currentCamera;
    }

    @Override
    public void setMapRenderer(NostalgiRenderer renderer) {
        this.mapRenderer = renderer;
    }

    @Override
    public NostalgiRenderer getMapRenderer() {
        return this.mapRenderer;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    private void replicateActors(HashMap<String, IActor> actors) {
        for(IActor actor : actors.values()) {
            if(actor.isReplicated()) {
                if(actor.getChildren() != null) {
                    replicateActors(actor.getChildren());
                }
            }
        }
    }

    private void tickActors(HashMap<String, IActor> actors, float delta) {
        for(Map.Entry<String, IActor> entry : actors.entrySet()) {
            IActor actor = entry.getValue();
            if(actor.canEverTick()) {
                actor.tick(delta);
                if(actor.getChildren() != null)
                    this.tickActors(actor.getChildren(), delta);
            }
        }

    }

    private void initInput() {

        // Set input processor to multiplexer
        inputProcessor = new InputMultiplexer();

        // Get and set all input processors.

        // Hud input
        if(this.getGameMode().getHud().getInputProcessor() != null)
            inputProcessor.addProcessor(this.getGameMode().getHud().getInputProcessor());

        // Set gesture input processor
        if (getGameMode().getCurrentController().getGestureListener() != null)
            inputProcessor.addProcessor(new GestureDetector(
                    getGameMode().getCurrentController().getGestureListener()));

        // Set standard input processor from controller
        if(getGameMode().getCurrentController().getInputProcessor() != null)
            inputProcessor.addProcessor(
                    getGameMode().getCurrentController().getInputProcessor());

        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void initMapActors() {
        this.getGameMode().getGameState().getCurrentLevel().initActors();
    }

    private void initMapWalls () {
        this.getGameMode().getGameState().getCurrentLevel().initWalls();
    }
}
