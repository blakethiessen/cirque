package com.oak.projectoak.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class FixturePositioningBody
{
    private Body body;
    private Vector2 fixturePositioningOffset;

    public FixturePositioningBody(Body body)
    {
        this.body = body;
        fixturePositioningOffset = Vector2.Zero;
    }

    public Body getBody()
    {
        return body;
    }

    public Vector2 getFixturePositioningOffset()
    {
        return fixturePositioningOffset;
    }

    public void addToFixturePositioningOffset(Vector2 newOffset)
    {
        this.fixturePositioningOffset.add(newOffset);
    }
}
