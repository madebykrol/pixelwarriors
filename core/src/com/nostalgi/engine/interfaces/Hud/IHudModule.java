package com.nostalgi.engine.interfaces.Hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by ksdkrol on 2016-07-12.
 */
public interface IHudModule {
    void update(float dTime);
    void draw(float dTime, Stage stage);
}