package com.oak.projectoak.physics;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.physics.userdata.FootSensorUD;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.physics.userdata.TrapUD;
import com.oak.projectoak.physics.userdata.UserData;

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

    public static Body createFloorBody()
    {
        Body body = b2world.createBody(Box2DDefs.FLOOR_BODY_DEF);

        body.createFixture(Box2DDefs.FLOOR_FIXTURE_DEF);

        return body;
    }

    public static Body createArenaCircleBody()
    {
        Body body = b2world.createBody(Box2DDefs.CIRCLE_BODY_DEF);

        body.createFixture(Box2DDefs.CIRCLE_FIXTURE_DEF);

        return body;
    }

    public static Body createStakeBody(float rotation)
    {
        Body body = b2world.createBody(Box2DDefs.STAKE_BODY_DEF);

        body.createFixture(Box2DDefs.STAKE_FIXTURE_DEF);
        body.setUserData(new TrapUD());

        System.out.println(rotation);

        return body;
    }
}
