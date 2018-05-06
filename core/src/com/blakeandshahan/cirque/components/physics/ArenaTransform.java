package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ArenaTransform implements Component, Pool.Poolable
{
    public float radialPosition;
    public boolean onOutsideEdge;

    public ArenaTransform init(float radialPosition, boolean onOutsideEdge)
    {
        this.radialPosition = radialPosition;
        this.onOutsideEdge = onOutsideEdge;

        return this;
    }

    @Override
    public void reset() {}
}
