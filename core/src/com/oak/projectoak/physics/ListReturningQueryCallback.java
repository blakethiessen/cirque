package com.oak.projectoak.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;

public class ListReturningQueryCallback implements QueryCallback
{
    private Array<Fixture> intersectingFixtures = new Array<Fixture>();

    @Override
    public boolean reportFixture(Fixture fixture)
    {
        intersectingFixtures.add(fixture);
        return true;
    }

    public Array<Fixture> getIntersectingFixtures()
    {
        return intersectingFixtures;
    }
}

