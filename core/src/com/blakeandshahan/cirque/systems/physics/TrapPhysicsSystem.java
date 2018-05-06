package com.blakeandshahan.cirque.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Render;
import com.blakeandshahan.cirque.components.physics.TrapPhysics;

public class TrapPhysicsSystem extends IteratingSystem
{
    public TrapPhysicsSystem()
    {
        super(Family.all(TrapPhysics.class).get());
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        Render render = Mapper.render.get(e);
        TrapPhysics trapPhysics = Mapper.trapPhysics.get(e);

        if (trapPhysics != null)
        {
            final float angle = trapPhysics.fixture.getBody().getAngle();
            render.setRotation((float)Math.toDegrees(angle + trapPhysics.initialRotation));
            final Vector2 newPosition = Constants.ConvertRadialTo2DPositionWithHeight(
                    trapPhysics.initialRadialPosition + angle, trapPhysics.onOutsideEdge, trapPhysics.trapHeight);
            newPosition.scl(Constants.METERS_TO_PIXELS);
            render.setPosition(newPosition);
        }
    }
}
