package com.oak.projectoak.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.Render.Layer;
import com.oak.projectoak.components.physics.Internal;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.physics.PhysicsFactory;

/*
    The EntityFactory is where all entities
    are composed of their components.
 */

public class EntityFactory
{
    // The master method for creating an entity. When writing
    // a new kind of entity, be sure to add a case and enum entry
    // to this method and the enum to be able to create it.
    public static Entity createEntity(EntityType type, World world, float x, float y)
    {
        switch(type)
        {
            case PLAYER:
                return createPlayer(world, x, y);
            case FLOOR:
                return createFloor(world, x, y);
            case CIRCLE:
                return createArenaCircle(world, x, y);
            default:
                Gdx.app.error("Unsupported Entity", "Entity of type " + type.toString() +
                        " has not been linked to a create method.");
                return world.createEntity();
        }
    }

    // Each entity type X must have a mapping to a corresponding createX method.
    public enum EntityType
    {
        PLAYER,
        FLOOR,
        CIRCLE
    }

    private static Entity createPlayer(World world, float x, float y)
    {
        Entity e = world.createEntity();

        e.addComponent(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), x, y));
        e.addComponent(new Controller());
        e.addComponent(new Platformer(3, 4, 30));
        e.addComponent(new Render("idle", Layer.ACTORS_3, x, y));
        e.addComponent(new Animate("idle"));
        e.addComponent(new Player());
        e.addComponent(new Internal());

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);

        e.addToWorld();

        return e;
    }

    private static Entity createFloor(World world, float x, float y)
    {
        Entity e = world.createEntity();

        e.addComponent(new Physics(PhysicsFactory.createFloorBody(), x, y));

        e.addToWorld();

        return e;
    }

    private static Entity createArenaCircle(World world, float x, float y)
    {
        Entity e = world.createEntity();

        e.addComponent(new Physics(PhysicsFactory.createArenaCircleBody(), x, y));

        e.addToWorld();

        return e;
    }

    private static Entity createBackground(World world)
    {
        Entity e = world.createEntity();

        e.addComponent(new Render("background", Layer.BACKGROUND, 0, 0));

        e.addToWorld();

        return e;
    }
}