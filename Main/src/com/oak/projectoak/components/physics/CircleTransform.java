package com.oak.projectoak.components.physics;

import com.artemis.Component;

public class CircleTransform extends Component
{
    public float radialPosition;
    public boolean onOutsideEdge;

    public CircleTransform(float radialPosition, boolean onOutsideEdge)
    {
        this.radialPosition = radialPosition;
        this.onOutsideEdge = onOutsideEdge;
    }
}
