package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.physics.TrapPhysics;

public class TrapPhysicsSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Render> rm;
    @Mapper ComponentMapper<TrapPhysics> tpm;

    public TrapPhysicsSystem()
    {
        super(Aspect.getAspectForAll(TrapPhysics.class));
    }

    @Override
    protected void process(Entity e)
    {
        Render render = rm.get(e);
        TrapPhysics trapPhysics = tpm.get(e);

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
