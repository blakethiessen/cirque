package com.oak.projectoak.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.Render.Layer;
import com.oak.projectoak.components.abilities.Stake;
import com.oak.projectoak.components.physics.CircleTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.physics.PhysicsFactory;

/*
    The EntityFactory is where all entities
    are composed of their components.
 */

public class EntityFactory
{
    public static Entity createPlayer(World world, float radialPosition, boolean onOutsideEdge)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        e.addComponent(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), twoDPosition));
        e.addComponent(new Player());
        e.addComponent(new Platformer(Constants.PLAYER_LAT_ACCEL,
                Constants.PLAYER_LAT_MAX_VEL, Constants.PLAYER_JUMP_ACCEL));
        e.addComponent(new Render("idle", Layer.ACTORS_3, twoDPosition));
        e.addComponent(new Animate(Constants.PIRATE_IDLE));
        e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.PIRATE));
        e.addComponent(new CircleTransform(radialPosition, onOutsideEdge));

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);

        e.addToWorld();

        return e;
    }

    public static Entity createFloor(World world, float radialPosition, boolean onOutsideEdge)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        e.addComponent(new Physics(PhysicsFactory.createFloorBody(), twoDPosition));

        e.addToWorld();

        return e;
    }

    public static Entity createArenaCircle(World world, Vector2 position)
    {
        Entity e = world.createEntity();

        e.addComponent(new Physics(PhysicsFactory.createArenaCircleBody(), position));
//        e.addComponent(new Render("circle", Layer.ACTORS_2, radialPosition, heightFromEdge));

        e.addToWorld();

        return e;
    }

    public static Entity createStake(World world, float radialPosition, boolean onOutsideEdge, float rotationInRadians)
    {
        Entity e = world.createEntity();

        if (onOutsideEdge)
            radialPosition += Constants.RADIAL_TRAP_OFFSET;
        else
            radialPosition -= Constants.RADIAL_TRAP_OFFSET;

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        twoDPosition.set(twoDPosition.x, twoDPosition.y);

        e.addComponent(new Stake());

        final Physics physics = new Physics(PhysicsFactory.createStakeBody(), twoDPosition);
        final Body body = physics.body;
        body.setTransform(body.getPosition(), rotationInRadians);
        e.addComponent(physics);

        e.addToWorld();

        return e;
    }

    public static Entity createBackground(World world)
    {
        Entity e = world.createEntity();

        e.addComponent(new Render("background", Layer.BACKGROUND, Vector2.Zero));

        e.addToWorld();

        return e;
    }
}