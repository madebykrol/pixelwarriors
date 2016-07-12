package com.nostalgi.engine.interfaces.States;

import com.badlogic.gdx.math.Vector2;
import com.nostalgi.engine.interfaces.IController;
import com.nostalgi.engine.interfaces.IGameMode;
import com.nostalgi.engine.interfaces.World.ILevel;

/**
 * Created by ksdkrol on 2016-07-03.
 */
public interface IGameState {

    com.nostalgi.engine.interfaces.World.ILevel getCurrentLevel();
    void setCurrentLevel(ILevel level);

    com.nostalgi.engine.interfaces.World.ICharacter getPlayerCharacter();

    void setCurrentGameMode(IGameMode mode);
    IGameMode getCurrentGameMode();

    void setCurrentController(IController controller);
    IController getCurrentController();

    void update(float delta);

    float getGameTime();

    Vector2 getGravity();
}