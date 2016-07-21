package com.nostalgi.engine.World;

import com.badlogic.gdx.math.Vector2;
import com.nostalgi.engine.interfaces.World.IActor;

/**
 * Created by Kristoffer on 2016-07-19.
 */
public class RootActor extends BaseActor {

    private float unitScale;

    public RootActor(Vector2 pos, float unitScale) {
        setPosition(pos);
        this.unitScale = unitScale;
    }

    public RootActor() {
        this(new Vector2(0,0), 32f);
    }

    @Override
    public Vector2 getPosition() {
        Vector2 worldPos = new Vector2(this.position.x * unitScale, this.position.y * unitScale);

        return worldPos;
    }
}
