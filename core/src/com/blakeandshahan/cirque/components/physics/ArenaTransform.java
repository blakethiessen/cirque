package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;

public class ArenaTransform extends Component
{
    public float radialPosition;
    public boolean onOutsideEdge;

    public ArenaTransform(float radialPosition, boolean onOutsideEdge)
    {
        this.radialPosition = radialPosition;
        this.onOutsideEdge = onOutsideEdge;
    }
}
