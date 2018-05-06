package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/*
    The Platformer component is attached to
    entities that can move around in a
    platformer/sidescrolling (mario, metroidvania)
    fashion.
 */

public class Platformer implements Component, Pool.Poolable
{
    public float latAccel;
    public float latMaxVel;
    public float jumpAccel;

    public ArrayList<Fixture> footContacts;

    public boolean jumpTimeoutOver;

    public Platformer init(float latAccel, float latMaxVel, float jumpAccel)
    {
        this.latAccel = latAccel;
        this.latMaxVel = latMaxVel;
        this.jumpAccel = jumpAccel;
        this.footContacts = new ArrayList<Fixture>();
        this.jumpTimeoutOver = true;

        return this;
    }

    public boolean isOnGround()
    {
        return !footContacts.isEmpty();
    }

    @Override
    public void reset() {}
}
