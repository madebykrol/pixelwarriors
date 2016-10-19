package com.nostalgi.engine.World;

import com.nostalgi.engine.interfaces.World.IActor;
import com.nostalgi.engine.interfaces.World.ICharacter;

/**
 * Created by Kristoffer on 2016-07-16.
 */
public class FloorChangerActor extends BaseActor {

    public FloorChangerActor() {

    }

    @Override
    public void onOverlapBegin(IActor overlapper) {
       if(overlapper instanceof ICharacter) {
           System.out.println("Derp");
           ICharacter character = (ICharacter)overlapper;
           character.setFloorLevel(this.getFloorLevel());
       }
    }

    @Override
    public void onOverlapEnd(IActor overlapper) {

    }
}
