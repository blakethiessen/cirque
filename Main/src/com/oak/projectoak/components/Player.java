package com.oak.projectoak.components;

import com.artemis.Component;
import com.oak.projectoak.Action;
import com.oak.projectoak.Constants;

/*
    The Player component is attached to entities
    that can be controlled in some way by a player, AI, etc.
 */

public class Player extends Component
{
    public int playerNum;

    public byte actionMask;

    public float mouseX;
    public float mouseY;

    public Player()
    {
        playerNum = ++Constants.curPlayersActive;

        actionMask = 0;
        this.mouseX = 0f;
        this.mouseY = 0f;
    }

    public void setAction(Action action, boolean state)
    {
        if (state)
            actionMask |= 1 << action.getId();
        else
            actionMask &= ~(1 << action.getId());
    }

    public boolean isActionOn(Action action)
    {
        return (actionMask & (1L << action.getId())) != 0;
    }
}
