package com.oak.projectoak.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.Render.Layer;
import com.oak.projectoak.components.abilities.Stake;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.physics.PhysicsFactory;

/*
    The EntityFactory is where all entities
    are composed of their components.
 */

public class EntityFactory
{
    public static Entity createText(World world, String text, Vector2 position)
    {
        Entity e = world.createEntity();

        e.addComponent(new TextRender(text, position));

        e.addToWorld();

        return e;
    }

    public static Entity createPlayer(World world, float radialPosition, boolean onOutsideEdge, int teamNum, Vector2 energyDisplayPosition)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        e.addComponent(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), twoDPosition));
        e.addComponent(new Player(teamNum));
        e.addComponent(new Platformer(Constants.PLAYER_LAT_ACCEL,
                Constants.PLAYER_LAT_MAX_VEL,
                onOutsideEdge ? Constants.OUTER_PLAYER_JUMP_ACCEL : Constants.INNER_PLAYER_JUMP_ACCEL));
        e.addComponent(new Render(Layer.ACTORS_3, twoDPosition, false));

        if (teamNum == 0)
        {
            e.addComponent(new Animate(Constants.PIRATE_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.PIRATE));
        }
        else
        {
            e.addComponent(new Animate(Constants.NINJA_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.NINJA));
        }

        e.addComponent(new ArenaTransform(radialPosition, onOutsideEdge));
        e.addComponent(new TextRender("", energyDisplayPosition));

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);

        e.addToWorld();

        return e;
    }

    private static Entity createUIEnergyBubble(World world, Entity playerEntity, Vector2 screenPosition)
    {
        Entity e = world.createEntity();

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

        e.addComponent(new DynamicPhysics(PhysicsFactory.createArenaCircleBody(), position));
        e.addComponent(new Arena());
        e.addComponent(new RenderOffset(new Vector2(Constants.ARENA_INNER_RADIUS / 2, Constants.ARENA_INNER_RADIUS / 2)));
        final Render render = new Render(Constants.OUTER_RING, Layer.ACTORS_2, position.cpy().sub(Constants.ARENA_INNER_RADIUS, Constants.ARENA_INNER_RADIUS), true);
        final Sprite sprite = render.sprites[0];
        sprite.setScale(2);

        e.addComponent(render);

        e.addToWorld();

        return e;
    }

    public static Entity createStake(World world, float radialPosition, boolean onOutsideEdge, float rotationInRadians)
    {
        Entity e = world.createEntity();

        if (onOutsideEdge)
            radialPosition += Constants.STAKE_SPAWN_OFFSET;
        else
            radialPosition -= Constants.STAKE_SPAWN_OFFSET;

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

        e.addComponent(new Render("background", Layer.BACKGROUND, Vector2.Zero, true));

        e.addToWorld();

        return e;
    }
}