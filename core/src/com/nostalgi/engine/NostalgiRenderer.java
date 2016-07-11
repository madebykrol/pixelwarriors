package com.nostalgi.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.nostalgi.engine.interfaces.ICharacter;
import com.nostalgi.engine.interfaces.IChest;
import com.nostalgi.engine.interfaces.IDoor;
import com.nostalgi.engine.interfaces.IItem;
import com.nostalgi.engine.interfaces.ILevel;
import com.nostalgi.engine.interfaces.IMonster;

import java.util.ArrayList;

/**
 * Created by Kristoffer on 2016-07-06.
 */
public class NostalgiRenderer extends OrthogonalTiledMapRenderer {

    private final String groundLayer;
    private ShapeRenderer shapeRenderer;
    private ILevel level;

    private ICharacter currentPlayer;
    private ArrayList<IItem> items;
    private ArrayList<ICharacter> NPCS;
    private ArrayList<IDoor> doors;
    private ArrayList<IMonster> monsters;
    private ArrayList<IChest> chests;


    public NostalgiRenderer(ILevel level, float unitScale) {
        this(level, unitScale, "Ground");
    }

    public NostalgiRenderer(ILevel level, float unitScale, String groundLayer) {
        super(level.getMap(), unitScale);
        this.level = level;
        this.groundLayer = groundLayer;
        shapeRenderer = new ShapeRenderer();
    }

    public Batch getBatch() {
        return this.batch;
    }

    @Override
    public void render() {
        beginRender();

        for (MapLayer layer : map.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                renderTileLayer((TiledMapTileLayer) layer);

                if (layer.getName().equals(groundLayer)) {
                    if(this.currentPlayer != null) {
                        this.getBatch().draw(new Texture(Gdx.files.internal("badlogic.jpg")),
                                this.currentPlayer.getPosition().x,
                                this.currentPlayer.getPosition().y,
                                this.currentPlayer.getHeight(),
                                this.currentPlayer.getWidth());
                    }
                }
            } else {
                for (MapObject object : layer.getObjects()) {
                    renderObject(object);
                }
            }
        }
        endRender();

    }

    public void setCurrentPlayerCharacter(ICharacter character) {
        this.currentPlayer = character;
    }

    protected void renderPlayer() {

    }

    protected void renderNPCS() {

    }

    protected void renderMonsters() {

    }

    protected void renderItems() {

    }

    protected void renderChests() {

    }
}