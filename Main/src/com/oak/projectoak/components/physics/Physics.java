package com.oak.projectoak.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/*
    The raw Physics component is for static
    unmoving physics objects that should affect/block
    other, moving physics objects.
 */

public class Physics extends Component
{
    public Body body;

    public Physics(Body body, Vector2 position)
    {
        this.body = body;

        body.setTransform(position.x, position.y, 0);
    }
}