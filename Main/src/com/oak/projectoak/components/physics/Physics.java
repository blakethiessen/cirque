package com.oak.projectoak.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.*;

/*
    The raw Physics component is for static
    unmoving physics objects that should affect/block
    other, moving physics objects.
 */

public class Physics extends Component
{
    public Body body;

    public Physics(Body body, float x, float y)
    {
        this.body = body;

        body.setTransform(x, y, 0);
    }
}