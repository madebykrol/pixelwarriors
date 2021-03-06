package com.nostalgi.engine.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Kristoffer on 2016-07-18.
 */
public class BoundingVolume {

    private boolean isSensor = false;
    private Shape shape = new PolygonShape();
    private short collisionMask = -1;
    private short collisionCategory = 0;
    private float density = 0f;
    private boolean autoRotate = false;
    private Vector2 relativePosition = new Vector2();
    private String volumeId;


    public short getCollisionCategory() {
        return collisionCategory;
    }

    public void setCollisionCategory(short collisionCategory) {
        this.collisionCategory = collisionCategory;
    }

    public short getCollisionMask() {
        return collisionMask;
    }

    public void setCollisionMask(short collisionMask) {
        this.collisionMask = collisionMask;
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public boolean isSensor() {
        return isSensor;
    }

    public void isSensor(boolean isSensor) {
        this.isSensor = isSensor;
    }

    public boolean autoRotate() {
        return autoRotate;
    }

    public void autoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
    }

    public void setRelativePosition(Vector2 relativePosition) { this.relativePosition = relativePosition; }

    public Vector2 getRelativePosition() { return this.relativePosition; }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }
}
