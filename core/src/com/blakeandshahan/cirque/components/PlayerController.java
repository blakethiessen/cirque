package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Action;

public class PlayerController implements Component, Pool.Poolable
{
    public int controllerNum;
    public byte actionMask;
    public byte previousUpdateActionMask;

    public boolean readyToBegin;

    public PlayerController init(int controllerNum)
    {
        this.controllerNum = controllerNum;

        actionMask = 0;
        readyToBegin = false;

        return this;
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

    public boolean actionHeld(Action action)
    {
        return checkMaskBit(actionMask, action.getId());
    }

    public boolean actionHeld(int actionId)
    {
        return checkMaskBit(actionMask, actionId);
    }

    public boolean actionDownOnce(Action action)
    {
        return actionHeld(action) && !checkMaskBit(previousUpdateActionMask, action.getId());
    }

    public boolean actionDownOnce(int actionId)
    {
        return actionHeld(actionId) && !checkMaskBit(previousUpdateActionMask, actionId);
    }

    private boolean checkMaskBit(byte mask, int actionId)
    {
        return (mask & (1L << actionId)) != 0;
    }

    @Override
    public void reset() {}
}
