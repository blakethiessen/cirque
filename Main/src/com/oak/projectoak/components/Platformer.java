package com.oak.projectoak.components;

import com.artemis.Component;

import java.util.Timer;
import java.util.TimerTask;

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

    public int footContactCount;

    public Timer jumpTimeout;
    public boolean jumpTimeoutOver;

    public Platformer(float latAccel, float latMaxVel, float jumpAccel)
    {
        this.latAccel = latAccel;
        this.latMaxVel = latMaxVel;
        this.jumpAccel = jumpAccel;
        this.footContactCount = 0;
        this.jumpTimeoutOver = true;

        jumpTimeout = new Timer();
    }
}
