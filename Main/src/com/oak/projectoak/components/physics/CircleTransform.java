package com.oak.projectoak.components.physics;

import com.artemis.Component;

public class CircleTransform extends Component
{
    public float radialPosition;
    public float distanceFromEdge;

    public CircleTransform(float radialPosition, float distanceFromEdge)
    {
        this.radialPosition = radialPosition;
        this.distanceFromEdge = distanceFromEdge;
    }
}
