package com.oak.projectoak.components;

import com.artemis.Component;
import com.oak.projectoak.Action;

/*
    The Player component is attached to entities
    that can be controlled in some way by a player, AI, etc.
 */

public class Player extends Component
{
    public int playerNum;
    public int controllerNum;

    public byte actionMask;

    public float mouseX;
    public float mouseY;

    public AbilityCreation[] abilities;

    public float lastCircularMovePosition;

    public boolean invulnerable;
    public boolean wasInvulnerableLastFrame;

    public float energyIncreaseMultiplier;
    public float lastLateralChangePosition;
    public boolean isMovingRight;

    //Player stats
    public int enemyKills, deaths, friendlyKills, teamNum;
    public String playerName;                                               //having this makes it easier to display name of player in scoreboard.
    private boolean havePlayerAttributesBeenSet, havePlayerAbilitiesBeenSet;


    //Does nothing except get us the controller methods (setAction and getAction)
    public Player(int controllerNum)
    {
        this.controllerNum = controllerNum;
        resetToControllerOnly();
    }


    public void resetToControllerOnly()
    {
        havePlayerAttributesBeenSet = havePlayerAbilitiesBeenSet = false;
        actionMask = 0;
        this.mouseX = 0f;
        this.mouseY = 0f;

        invulnerable = false;
        wasInvulnerableLastFrame = false;
        energyIncreaseMultiplier = 1;
        friendlyKills = enemyKills = deaths = 0;
        lastLateralChangePosition = 0f;

        playerNum = teamNum = 0;
        playerName = "N/A";
    }

    //sets player's attributes
    public void setPlayerAttributes(int playerNum, int teamNum, String playerName)
    {
        this.teamNum = teamNum;
        this.playerNum = playerNum;
        this.playerName = playerName;
        isMovingRight = true;

        havePlayerAttributesBeenSet = true;
    }


    //set's player's abilities
    public void setAbilities(AbilityCreation[] abilityCreationComponents)
    {
        this.abilities = abilityCreationComponents;
        havePlayerAbilitiesBeenSet = true;
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


    public boolean havePlayerAbilitiesBeenSet()
    {
        return havePlayerAbilitiesBeenSet;
    }

    public boolean havePlayerAttributesBeenSet()
    {
        return havePlayerAttributesBeenSet;
    }

}
