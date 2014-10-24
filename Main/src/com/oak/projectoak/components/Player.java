package com.oak.projectoak.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.oak.projectoak.Action;

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

    public AbilityCreation[] abilities;

    public float lastCircularMovePosition;

    public int teamNum;

    public boolean invulnerable;
    public boolean wasInvulnerableLastFrame;

    public float energyIncreaseMultiplier;
    public float lastLateralChangePosition;
    public boolean isMovingRight;

    public Render portraitRender;
    public Portrait portraitPortrait;

    //Player stats
    public int enemyKills, deaths, friendlyKills;

    public Player(
            int playerNum, int teamNum, AbilityCreation[] abilityCreationComponents, Entity characterPortrait)
    {
        this.teamNum = teamNum;
        this.abilities = abilityCreationComponents;
        this.portraitRender = characterPortrait.getComponent(Render.class);
        this.portraitPortrait = characterPortrait.getComponent(Portrait.class);

        this.playerNum = playerNum;

        actionMask = 0;
        this.mouseX = 0f;
        this.mouseY = 0f;

        invulnerable = false;
        wasInvulnerableLastFrame = false;
        energyIncreaseMultiplier = 1;
        lastLateralChangePosition = 0f;
        isMovingRight = true;

        friendlyKills = enemyKills = deaths = 0;
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
