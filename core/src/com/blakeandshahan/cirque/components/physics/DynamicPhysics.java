package com.blakeandshahan.cirque.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/*
    DynamicPhysics is added to physics objects
    that can can be moved and pushed around in the world.
 */

public class DynamicPhysics extends Physics implements Pool.Poolable
{
    public Vector2 curGravityVec = Vector2.Zero;

    public DynamicPhysics init(Body body, Vector2 position)
    {
        return (DynamicPhysics)super.init(body, position);
    }

    @Override
    public void reset()
    {
        super.reset();

        curGravityVec = Vector2.Zero;
    }
}
