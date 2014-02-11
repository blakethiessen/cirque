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
           case PIRATE:
               idle = Constants.PIRATE_IDLE;
               walk = Constants.PIRATE_WALK;
               jump = Constants.PIRATE_JUMP;
               layTrap = Constants.PIRATE_LAY_TRAP;
               selfDestruct = Constants.PIRATE_SELF_DESTRUCT;
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
        PIRATE
    }
}
