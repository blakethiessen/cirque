package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ArenaRotation implements Component, Pool.Poolable
{
    public float rotationVelocity;

    public ArenaRotation init()
    {
        return this;
    }

    @Override
    public void reset() {}
}
