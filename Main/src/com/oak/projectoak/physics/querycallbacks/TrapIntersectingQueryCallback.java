package com.oak.projectoak.physics.querycallbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.oak.projectoak.physics.userdata.TrapUD;

public class TrapIntersectingQueryCallback implements QueryCallback
{
    private boolean trapIsIntersecting;

    @Override
    public boolean reportFixture(Fixture fixture)
    {
        if (fixture.getBody().getUserData() instanceof TrapUD)
        {
            trapIsIntersecting = true;
            return false;
        }

        return true;
    }

    public boolean isTrapIntersecting()
    {
        return trapIsIntersecting;
    }
}
