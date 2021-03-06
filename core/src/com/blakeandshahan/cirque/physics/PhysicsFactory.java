package com.blakeandshahan.cirque.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.blakeandshahan.cirque.physics.userdata.*;

/*
    The PhysicsFactory takes the pieces from Box2DDefs
    and puts them in a convenient construction function.
 */

public class PhysicsFactory
{
    private static World b2world;

    public static void setWorld(World world)
    {
        b2world = world;
    }

    public static Body createRunnerBody(Entity e)
    {
        Body body = b2world.createBody(Box2DDefs.PLAYER_BODY_DEF);

        body.createFixture(Box2DDefs.PLAYER_TORSO);
        UserData runnerUD = new PlayerUD(e);
        body.setUserData(runnerUD);

        Fixture sensorFixture = body.createFixture(Box2DDefs.PLAYER_FOOT_SENSOR);
        sensorFixture.setUserData(new FootSensorUD());

        return body;
    }

    public static Body createTrapRingBody()
    {
        Body body = b2world.createBody(Box2DDefs.TRAP_RING_BODY_DEF);

        return body;
    }

    public static Body createArenaCircleBody()
    {
        Body body = b2world.createBody(Box2DDefs.ARENA_BODY_DEF);

        body.setUserData(new ArenaUD());

        body.createFixture(Box2DDefs.INNER_CIRCLE_FIXTURE_DEF);
        body.createFixture(Box2DDefs.OUTER_CIRCLE_FIXTURE_DEF);

        return body;
    }

    public static Fixture createTrapFixture(FixtureDef fixtureDef, Body body, Shape shape)
    {
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        return fixture;
    }

    public static Body createLightningBoltBody(Entity e)
    {
        Body body = b2world.createBody(Box2DDefs.GENERIC_ABILITY_BODY_DEF);

        Fixture fixture = body.createFixture(Box2DDefs.LIGHTNING_BOLT_FIXTURE_DEF);

        fixture.setUserData(new LightningUD(e));

        return body;
    }
}
