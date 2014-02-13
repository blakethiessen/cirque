package com.oak.projectoak.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
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
    public static Entity createPlayer(World world, float radialPosition, float heightFromEdge)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, heightFromEdge);

        e.addComponent(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), twoDPosition));
        e.addComponent(new Player());
        e.addComponent(new Platformer(Constants.PLAYER_LAT_ACCEL,
                Constants.PLAYER_LAT_MAX_VEL, Constants.PLAYER_JUMP_ACCEL));
        e.addComponent(new Render("idle", Layer.ACTORS_3, twoDPosition));
        e.addComponent(new Animate(Constants.PIRATE_IDLE));
        e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.PIRATE));
        e.addComponent(new CircleTransform(radialPosition, heightFromEdge));

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);

        e.addToWorld();

        return e;
    }

    public static Entity createFloor(World world, float radialPosition, float heightFromEdge)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, heightFromEdge);

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

    public static Entity createStake(World world, float radialPosition, float heightFromEdge, float rotation)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, heightFromEdge);

        // Adjust to position at center
        twoDPosition.set(twoDPosition.x - Constants.STAKE_WIDTH / 2, twoDPosition.y - Constants.STAKE_WIDTH / 2);

        e.addComponent(new Stake());
        e.addComponent(new Physics(PhysicsFactory.createStakeBody(rotation), twoDPosition));

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