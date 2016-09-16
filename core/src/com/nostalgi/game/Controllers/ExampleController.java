package com.nostalgi.game.Controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.nostalgi.engine.BaseController;
import com.nostalgi.engine.Direction;
import com.nostalgi.engine.Render.NostalgiCamera;
import com.nostalgi.engine.States.AnimationStates;
import com.nostalgi.engine.interfaces.World.IActor;
import com.nostalgi.engine.interfaces.World.ICharacter;

import java.util.ArrayList;

/**
 * Created by ksdkrol on 2016-09-14.
 */
public class ExampleController extends BaseController {

    private boolean leftIsPressed = false;
    private boolean rightIsPressed = false;
    private boolean upIsPressed = false;
    private boolean downIsPressed = false;

    public ExampleController(NostalgiCamera camera, World world) {
        super(camera, world);
    }

    @Override
    public void update(float dTime) {

        ICharacter currentPossessedCharacter = this.getCurrentPossessedCharacter();

        if (currentPossessedCharacter != null) {
            currentPossessedCharacter.stop();
            currentPossessedCharacter.setWalkingState(AnimationStates.IdleFaceSouthAnimation);

            boolean moving = false;
            float faceDirection = currentPossessedCharacter.getFacingDirection();

            if(faceDirection == Direction.SOUTH)
                currentPossessedCharacter.setWalkingState(AnimationStates.IdleFaceSouthAnimation);

            if(faceDirection == Direction.EAST)
                currentPossessedCharacter.setWalkingState(AnimationStates.IdleFaceEastAnimation);

            if(faceDirection == Direction.WEST)
                currentPossessedCharacter.setWalkingState(AnimationStates.IdleFaceWestAnimation);

            if(faceDirection == Direction.NORTH)
                currentPossessedCharacter.setWalkingState(AnimationStates.IdleFaceNorthAnimation);

            if(upIsPressed && rightIsPressed) {
                currentPossessedCharacter.face(Direction.NORTH_EAST);
            }

            if(downIsPressed && rightIsPressed) {
                currentPossessedCharacter.face(Direction.SOUTH_EAST);
            }

            if(upIsPressed && leftIsPressed) {
                currentPossessedCharacter.face(Direction.NORTH_WEST);
            }

            if(downIsPressed && rightIsPressed) {
                currentPossessedCharacter.face(Direction.SOUTH_WEST);
            }

            if (leftIsPressed) {
                moving = true;
                currentPossessedCharacter.setWalkingState(AnimationStates.WalkingWestAnimation);
            }
            if (rightIsPressed) {
                moving = true;
                currentPossessedCharacter.setWalkingState(AnimationStates.WalkingEastAnimation);
            }
            if (upIsPressed) {
                moving = true;
                currentPossessedCharacter.setWalkingState(AnimationStates.WalkingNorthAnimation);
            }
            if (downIsPressed) {
                moving = true;
                currentPossessedCharacter.setWalkingState(AnimationStates.WalkingSouthAnimation);
            }

            //this.currentPossessedCharacter.face(new Vector2(32,32));

            if((moving)) {
                currentPossessedCharacter.moveForward(5);
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldPos = getCamera().unproject(new Vector3(screenX, screenY, 0));

        Vector2 worldPos2D = new Vector2(worldPos.x, worldPos.y);

        ArrayList<IActor> actors = actorsCloseToLocation(worldPos2D, 0f);
        if(!actors.isEmpty()) {
            IActor topActor = actors.get(0);
            if(topActor == this.getCurrentPossessedCharacter()) {
                System.out.println("Clicked player - Open character wheel");
            }
        }

        return false;
    }


    @Override
    public boolean keyDown(int keycode) {

        ICharacter currentPossessedCharacter = this.getCurrentPossessedCharacter();

        if(keycode == Input.Keys.LEFT) {
            this.leftIsPressed = true;
            currentPossessedCharacter.face(Direction.WEST);
        }
        if(keycode == Input.Keys.RIGHT) {
            this.rightIsPressed = true;
            currentPossessedCharacter.face(Direction.EAST);
        }
        if(keycode == Input.Keys.UP) {
            this.upIsPressed = true;
            currentPossessedCharacter.face(Direction.NORTH);
        }
        if(keycode == Input.Keys.DOWN) {
            this.downIsPressed = true;
            currentPossessedCharacter.face(Direction.SOUTH);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            this.leftIsPressed = false;
        if(keycode == Input.Keys.RIGHT)
            this.rightIsPressed = false;
        if(keycode == Input.Keys.UP)
            this.upIsPressed = false;
        if(keycode == Input.Keys.DOWN)
            this.downIsPressed = false;
        return true;
    }
}