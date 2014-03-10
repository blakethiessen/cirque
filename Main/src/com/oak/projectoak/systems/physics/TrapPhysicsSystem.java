package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
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

        final float angle = trapPhysics.fixture.getBody().getAngle();
        render.setRotation((float)Math.toDegrees(angle + trapPhysics.initialRotation));
    }
}
