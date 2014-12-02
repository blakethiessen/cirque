package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.blakeandshahan.cirque.Action;

public class PlayerController extends Component
{
    public int controllerNum;
    public byte actionMask;

    public boolean startButtonHeld;

    public boolean readyToBegin;

    public PlayerController(int controllerNum)
    {
        this.controllerNum = controllerNum;

        actionMask = 0;
        startButtonHeld = false;
        readyToBegin = false;
    }

    public void setAction(Action action, boolean state)
    {
        if (state)
            actionMask |= 1 << action.getId();
        else
            actionMask &= ~(1 << action.getId());
    }

    public void resetActions()
    {
        actionMask = 0;
    }

    public boolean isActionOn(Action action)
    {
        return (actionMask & (1L << action.getId())) != 0;
    }

    public boolean isActionOn(int actionId)
    {
        return (actionMask & (1L << actionId)) != 0;
    }
}
