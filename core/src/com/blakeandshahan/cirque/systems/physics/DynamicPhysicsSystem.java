package com.blakeandshahan.cirque.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Render;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;

// Updates draw component position with dynamic physics component position.
public class DynamicPhysicsSystem extends IteratingSystem
{
    public DynamicPhysicsSystem()
    {
        super(Family.getFor(DynamicPhysics.class, Render.class));
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        Render render = Mapper.render.get(e);
        DynamicPhysics physics = Mapper.dynamicPhysics.get(e);

        if (render != null && physics != null)
        {
            Body body = physics.body;
            Vector2 position = body.getPosition();

            Vector2 scaledPosition = position.cpy().scl(Constants.METERS_TO_PIXELS);
            render.setPosition(new Vector2(scaledPosition.x, scaledPosition.y));
            render.setRotation((float)Math.toDegrees(body.getAngle()));
        }
    }
}
