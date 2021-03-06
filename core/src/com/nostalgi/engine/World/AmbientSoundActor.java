package com.nostalgi.engine.World;

import com.nostalgi.engine.Annotations.NostalgiField;
import com.nostalgi.engine.World.Audio.ISound;
import com.nostalgi.engine.World.Audio.ISoundSystem;
import com.nostalgi.engine.interfaces.World.IActor;
import com.nostalgi.engine.interfaces.World.IWorld;
import com.nostalgi.engine.physics.BoundingVolume;

import java.util.ArrayList;

/**
 * Created by Krille on 15/11/2016.
 */

public class AmbientSoundActor extends BaseActor {

    @NostalgiField(fieldName = "SoundWave", withMethod = "createSound")
    private String soundWave;

    @NostalgiField(fieldName = "Loop")
    private boolean loop;

    @NostalgiField(fieldName = "Radius")
    private float radius;

    @NostalgiField(fieldName = "Falloff Distance")
    private float falloffDistance;

    private ISound sound;
    private ISound.ISoundReference soundRef;

    private IWorld world;


    public AmbientSoundActor(IWorld world, ArrayList<BoundingVolume> boundingVolumes) {
        super(boundingVolumes);
        this.world = world;

        this.canEverTick = true;
    }

    public AmbientSoundActor(IWorld world, String soundWave, ArrayList<BoundingVolume> boundingVolumes, boolean loop, float radius, float falloffDistance) {
        this(world, boundingVolumes);

        this.soundWave = soundWave;
        this.loop = loop;
        this.radius = radius;
        this.falloffDistance = falloffDistance;

        createSound(soundWave);
    }

    public void createSound(String soundWave) {
        this.sound = this.world.getSoundSystem().createSound(soundWave);
    }

    public void tick(float deltaTime) {
        ISoundSystem soundSystem = this.world.getSoundSystem();
        IActor relativeToCheck = this.world.getCurrentController().getCurrentPossessedCharacter();
        if(this.getPosition().dst(relativeToCheck.getPosition()) <= this.radius+this.falloffDistance) {
            if(this.soundRef == null) {
                this.soundRef = this.sound.play(0f,
                        1f, soundSystem.calculatePan(this.getPosition(), relativeToCheck.getPosition()));
                this.soundRef.setLooping(this.loop);
            }  else {
                this.soundRef.setPan(
                    soundSystem.calculatePan(this.getPosition(), relativeToCheck.getPosition()),
                    soundSystem.calculateVolume(this.radius, this.falloffDistance, this.getPosition(), relativeToCheck.getPosition()));
            }
        } else {

            if(this.soundRef != null) {
                this.soundRef.stop();
                this.soundRef = null;
            }
        }
    }

    @Override
    public void destroy() {
        if(this.sound != null) {
            this.sound.dispose();
        }
    }
}
