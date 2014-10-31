package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.ArrayList;

/*
    The Platformer component is attached to
    entities that can move around in a
    platformer/sidescrolling (mario, metroidvania)
    fashion.
 */

public class Platformer extends Component
{
    public float latAccel;
    public float latMaxVel;
    public float jumpAccel;

    public ArrayList<Fixture> footContacts;

    public boolean jumpTimeoutOver;

    public Platformer(float latAccel, float latMaxVel, float jumpAccel)
    {
        this.latAccel = latAccel;
        this.latMaxVel = latMaxVel;
        this.jumpAccel = jumpAccel;
        this.footContacts = new ArrayList<Fixture>();
        this.jumpTimeoutOver = true;
    }

    public boolean isOnGround()
    {
        return !footContacts.isEmpty();
    }
}
