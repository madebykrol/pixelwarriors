package com.nostalgi.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nostalgi.engine.BaseController;
import com.nostalgi.engine.BaseGameMode;
import com.nostalgi.engine.BaseGameState;
import com.nostalgi.engine.BaseHud;
import com.nostalgi.engine.BasePlayerCharacter;
import com.nostalgi.engine.Factories.NostalgiAnimationFactory;
import com.nostalgi.engine.Hud.DebugHudModule;
import com.nostalgi.engine.Hud.DemoHudModule;
import com.nostalgi.engine.States.AnimationStates;
import com.nostalgi.engine.interfaces.Factories.IAnimationFactory;
import com.nostalgi.game.levels.GrassLandLevel;
import com.nostalgi.engine.NostalgiBaseEngine;
import com.nostalgi.engine.NostalgiRenderer;
import com.nostalgi.engine.interfaces.World.ICharacter;
import com.nostalgi.engine.interfaces.IController;
import com.nostalgi.engine.interfaces.IGameEngine;
import com.nostalgi.engine.interfaces.IGameMode;
import com.nostalgi.engine.interfaces.States.IGameState;
import com.nostalgi.engine.interfaces.Hud.IHud;
import com.nostalgi.render.NostalgiCamera;

public class ExampleGame extends ApplicationAdapter {

	NostalgiCamera camera;
	NostalgiRenderer tiledMapRenderer;
	IController playerController;
	Viewport viewport;

	int w;
	int h;

	IGameMode gameMode;
	IGameState gameState;
	IGameEngine gameEngine;

	boolean headless = false;

	IAnimationFactory animationFactory;

	public ExampleGame(boolean headless) {
		this.headless = headless;
	}

	@Override
	public void create () {

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		this.gameState = new BaseGameState(
				new GrassLandLevel(new TmxMapLoader()));

		camera = new NostalgiCamera(
				w, h,
				gameState.getCurrentLevel().getCameraBounds(),
				gameState.getCurrentLevel().getTileSize());
		camera.setPositionSafe(gameState.getCurrentLevel().getCameraInitLocation());
		viewport = new StretchViewport(h, w, camera);

		this.playerController = new BaseController();
		this.playerController.possessCharacter(createPlayerCharacter());

		this.gameState.setCurrentController(this.playerController);
		this.gameMode = new BaseGameMode(this.gameState);

		this.tiledMapRenderer = new NostalgiRenderer(
				gameState.getCurrentLevel(),
				1 / (float)gameState.getCurrentLevel().getTileSize());

		this.gameEngine = new NostalgiBaseEngine(this.gameState, this.gameMode, tiledMapRenderer);
		this.gameEngine.setCurrentCamera(camera);

		IHud hud = new BaseHud(w/2, h/2, this.gameState);
		//hud.addModule("Demo", new DemoHudModule());
		hud.addModule("Debug", new DebugHudModule());
		hud.init();

		this.gameEngine.setHud(hud);
		this.gameEngine.init();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameEngine.update();
		gameEngine.render();
	}

	@Override
	public void resize(int width, int height) {

	}

	private ICharacter createPlayerCharacter() {
		ICharacter playerCharacter = new BasePlayerCharacter(new Vector2(10,58));
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