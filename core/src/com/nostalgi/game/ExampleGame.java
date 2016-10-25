package com.nostalgi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nostalgi.engine.BaseGame;
import com.nostalgi.engine.BaseGameMode;
import com.nostalgi.engine.BaseGameState;
import com.nostalgi.engine.BaseHud;
import com.nostalgi.engine.BasePlayerCharacter;
import com.nostalgi.engine.Factories.NostalgiActorFactory;
import com.nostalgi.engine.Factories.NostalgiAnimationFactory;
import com.nostalgi.engine.Factories.NostalgiWallFactory;
import com.nostalgi.engine.Hud.DebugHudModule;
import com.nostalgi.engine.BasePlayerState;
import com.nostalgi.engine.Hud.DemoHudModule;
import com.nostalgi.engine.States.AnimationStates;
import com.nostalgi.engine.World.NostalgiWorld;
import com.nostalgi.engine.World.RootActor;
import com.nostalgi.engine.interfaces.Factories.IAnimationFactory;
import com.nostalgi.engine.interfaces.States.IPlayerState;
import com.nostalgi.engine.interfaces.World.ILevel;
import com.nostalgi.engine.interfaces.World.IWorld;
import com.nostalgi.game.Controllers.ExampleController;
import com.nostalgi.game.levels.GrassLandLevel;
import com.nostalgi.engine.NostalgiBaseEngine;
import com.nostalgi.engine.NostalgiRenderer;
import com.nostalgi.engine.interfaces.World.ICharacter;
import com.nostalgi.engine.interfaces.IController;
import com.nostalgi.engine.interfaces.IGameMode;
import com.nostalgi.engine.interfaces.States.IGameState;
import com.nostalgi.engine.interfaces.Hud.IHud;
import com.nostalgi.engine.Render.NostalgiCamera;

public class ExampleGame extends BaseGame {

	NostalgiCamera camera;
	NostalgiRenderer tiledMapRenderer;
	IController playerController;
	Viewport viewport;

	int w;
	int h;

	IGameMode gameMode;
	IGameState gameState;
	IPlayerState playerState;

	IAnimationFactory animationFactory;
	World world;

	public ExampleGame(boolean headless) {
		super(headless, false);
	}


	@Override
	public void create () {

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		// setup Playerstate
		playerState = new BasePlayerState();

		// setup Game state
		gameState = new BaseGameState();
		gameState.addPlayerState(playerState);


		// setup physics world
		IHud hud = new BaseHud(w/2, h/2);
		hud.addModule("Demo", new DemoHudModule());
		hud.addModule("Debug", new DebugHudModule());
		hud.init();

		IWorld nostalgiWorld = new NostalgiWorld(new World(gameState.getGravity(), true), gameMode);

		// Setup start level
		ILevel grassland = new GrassLandLevel(new TmxMapLoader(), new NostalgiActorFactory(nostalgiWorld), new NostalgiWallFactory(nostalgiWorld));

		camera = new NostalgiCamera(
				w, h,
				grassland.getCameraBounds(),
				grassland.getTileSize());

		playerController = new ExampleController(this.camera, nostalgiWorld, hud);

		this.gameMode = new BaseGameMode(this.gameState, this.playerController, hud);




		// setup map renderer.
		tiledMapRenderer = new NostalgiRenderer(
				grassland,
				1 / (float)grassland.getTileSize());

		gameState.setCurrentLevel(grassland);

		camera.setPositionSafe(grassland.getCameraInitLocation());
		viewport = new StretchViewport(h, w, camera);


		this.gameEngine = new NostalgiBaseEngine(nostalgiWorld, camera, tiledMapRenderer);

		this.playerController.possessCharacter(createPlayerCharacter());

		this.gameEngine.init();
	}


	private ICharacter createPlayerCharacter() {
		ICharacter playerCharacter = new BasePlayerCharacter();
		playerCharacter.setPosition(new Vector2(9, 53));
		playerCharacter.setParent(new RootActor());
		//playerCharacter.setWorld();
		animationFactory = new NostalgiAnimationFactory();

		playerCharacter.addAnimation(AnimationStates.WalkingEastAnimation,
				animationFactory.createAnimation("Spritesheet/char_walk_east.png",
						32, 64, 1, 2, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.WalkingWestAnimation,
				animationFactory.createAnimation("Spritesheet/char_walk_west.png",
						32, 64, 1, 2, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.WalkingNorthAnimation,
				animationFactory.createAnimation("Spritesheet/char_walk_north.png",
						32, 64, 1, 5, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.WalkingSouthAnimation,
				animationFactory.createAnimation("Spritesheet/char_walk_south.png",
						32, 64, 1, 5, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.IdleFaceSouthAnimation,
				animationFactory.createAnimation("Spritesheet/char_idle.png",
						32, 64, 1, 1, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.IdleFaceNorthAnimation,
				animationFactory.createAnimation("Spritesheet/char_idle_north.png",
						32, 64, 1, 1, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.IdleFaceEastAnimation,
				animationFactory.createAnimation("Spritesheet/char_idle_east.png",
						32, 64, 1, 1, 1f / 6f,
						Animation.PlayMode.LOOP));

		playerCharacter.addAnimation(AnimationStates.IdleFaceWestAnimation,
				animationFactory.createAnimation("Spritesheet/char_idle_west.png",
						32, 64, 1, 1, 1f / 6f,
						Animation.PlayMode.LOOP));

		return playerCharacter;
	}

	@Override
	public void dispose() {
		this.animationFactory.dispose();
		this.gameEngine.dispose();
	}

}
