package com.oak.projectoak.components;

import com.artemis.Component;
import com.oak.projectoak.Constants;

public class PlayerAnimation extends Component
{
    public String idle;
    public String walk;
    public String jump;
    public String layTrap;
    public String selfDestruct;

    public PlayerAnimation(AnimationSet animationSet)
    {
       switch (animationSet)
       {
           case NINJA:
               idle = Constants.NINJA_IDLE;
               walk = Constants.NINJA_WALK;
               jump = Constants.NINJA_JUMP;
               layTrap = Constants.NINJA_LAY_TRAP;
               selfDestruct = Constants.NINJA_SELF_DESTRUCT;
               break;
           case PIRATE:
               idle = Constants.PIRATE_IDLE;
               walk = Constants.PIRATE_WALK;
               jump = Constants.PIRATE_JUMP;
               layTrap = Constants.PIRATE_LAY_TRAP;
               selfDestruct = Constants.PIRATE_SELF_DESTRUCT;
               break;
           case GANGSTA:
               idle = Constants.GANGSTA_IDLE;
               walk = Constants.GANGSTA_WALK;
               jump = Constants.GANGSTA_JUMP;
               layTrap = Constants.GANGSTA_LAY_TRAP;
               selfDestruct = Constants.GANGSTA_SELF_DESTRUCT;
               break;
           case PHARAOH:
               idle = Constants.PHARAOH_IDLE;
               walk = Constants.PHARAOH_WALK;
               jump = Constants.PHARAOH_JUMP;
               layTrap = Constants.PHARAOH_LAY_TRAP;
               selfDestruct = Constants.PHARAOH_SELF_DESTRUCT;
               break;
           default:
               idle = Constants.SHAHAN_IDLE;
               walk = Constants.SHAHAN_WALK;
               break;
       }
    }

    public enum AnimationSet
    {
        SHAHAN,
        PIRATE,
        GANGSTA, PHARAOH, NINJA
    }
}
