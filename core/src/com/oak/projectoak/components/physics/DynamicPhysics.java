package com.oak.projectoak.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/*
    DynamicPhysics is added to physics objects
    that can can be moved and pushed around in the world.
 */

public class DynamicPhysics extends Physics
{
    public Vector2 curGravityVec;

    public DynamicPhysics(Body body, Vector2 position)
    {
        super(body, position);

        curGravityVec = Vector2.Zero;
    }
}