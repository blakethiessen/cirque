package com.oak.projectoak.components.physics;

import com.artemis.Component;

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
