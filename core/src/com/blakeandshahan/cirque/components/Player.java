package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.entity.EntityFactory;

/*
    The Player component is attached to entities
    that can be controlled in some way by a player, AI, etc.
 */

public class Player extends Component implements Pool.Poolable
{
    // TODO: Remove these
    public float mouseX;
    public float mouseY;

    public int teamNum;

    public boolean invulnerable;
    public boolean wasInvulnerableLastFrame;

    public float energyIncreaseMultiplier;
    public float lastLateralChangePosition;
    public boolean isMovingRight;

    public Entity portrait;
    public Render portraitRender;
    public Portrait portraitPortrait;

    //Player stats
    public int enemyKills, deaths, friendlyKills;

    public Player init(int teamNum, Entity characterPortrait)
    {
        reset();

        this.teamNum = teamNum;

        this.portrait = characterPortrait;
        this.portraitRender = Mapper.render.get(characterPortrait);
        this.portraitPortrait = Mapper.portrait.get(characterPortrait);

        this.mouseX = 0f;
        this.mouseY = 0f;


        return this;
    }

    @Override
    public void reset()
    {
        invulnerable = false;
        wasInvulnerableLastFrame = false;
        energyIncreaseMultiplier = 1;
        lastLateralChangePosition = 0f;
        isMovingRight = true;

        friendlyKills = enemyKills = deaths = 0;

        if (portrait != null)
            EntityFactory.engine.removeEntity(portrait);
    }
}
