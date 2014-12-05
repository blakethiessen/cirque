package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Constants;

public class PlayerAnimation extends Component implements Pool.Poolable
{
    public String idle;
    public String walk;
    public String jump;
    public String layTrap;
    public String death;

    public PlayerAnimation init(AnimationSet animationSet)
    {
       switch (animationSet)
       {
           case NINJA:
               idle = Constants.NINJA_IDLE;
               walk = Constants.NINJA_WALK;
               jump = Constants.NINJA_JUMP;
               layTrap = Constants.NINJA_LAY_TRAP;
               death = Constants.NINJA_DEATH;
               break;
           case PIRATE:
               idle = Constants.PIRATE_IDLE;
               walk = Constants.PIRATE_WALK;
               jump = Constants.PIRATE_JUMP;
               layTrap = Constants.PIRATE_LAY_TRAP;
               death = Constants.PIRATE_DEATH;
               break;
           case GANGSTA:
               idle = Constants.GANGSTA_IDLE;
               walk = Constants.GANGSTA_WALK;
               jump = Constants.GANGSTA_JUMP;
               layTrap = Constants.GANGSTA_LAY_TRAP;
               death = Constants.GANGSTA_DEATH;
               break;
           case PHARAOH:
               idle = Constants.PHARAOH_IDLE;
               walk = Constants.PHARAOH_WALK;
               jump = Constants.PHARAOH_JUMP;
               layTrap = Constants.PHARAOH_LAY_TRAP;
               death = Constants.PHARAOH_DEATH;
               break;
           default:
               idle = Constants.SHAHAN_IDLE;
               walk = Constants.SHAHAN_WALK;
               break;
       }

        return this;
    }

    @Override
    public void reset() {}

    public enum AnimationSet
    {
        SHAHAN,
        PIRATE,
        GANGSTA, PHARAOH, NINJA
    }
}
