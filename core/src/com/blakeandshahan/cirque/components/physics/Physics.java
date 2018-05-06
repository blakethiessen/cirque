package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/*
    The raw Physics component is for static
    unmoving physics objects that should affect/block
    other, moving physics objects.
 */

public class Physics implements Component, Pool.Poolable
{
    public Body body;

    public Physics init(Body body, Vector2 position)
    {
        this.body = body;

        body.setTransform(position.x, position.y, 0);

        return this;
    }

    @Override
    public void reset() {}
}