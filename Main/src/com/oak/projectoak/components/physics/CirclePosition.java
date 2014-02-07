package com.oak.projectoak.components.physics;

import com.artemis.Component;

public class CirclePosition extends Component
{
    public float radialPosition;
    public float distanceFromEdge;

    public CirclePosition(float radialPosition, float distanceFromEdge)
    {
        this.radialPosition = radialPosition;
        this.distanceFromEdge = distanceFromEdge;
    }
}
