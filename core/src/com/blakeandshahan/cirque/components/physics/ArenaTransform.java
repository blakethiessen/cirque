package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ArenaTransform extends Component implements Pool.Poolable
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
