package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.TrapPhysics;

public class TrapPhysicsSystem extends EntityProcessingSystem
{
    private final DynamicPhysics trapRingPhysics;
    @Mapper ComponentMapper<Render> rm;
    @Mapper ComponentMapper<TrapPhysics> tpm;

    public TrapPhysicsSystem(DynamicPhysics trapRingPhysics)
    {
        super(Aspect.getAspectForAll(TrapPhysics.class));
        this.trapRingPhysics = trapRingPhysics;
    }

    @Override
    protected void process(Entity e)
    {
        Render render = rm.get(e);
        TrapPhysics trapPhysics = tpm.get(e);

        final float angle = trapPhysics.fixture.getBody().getAngle();
        render.setRotation((float)Math.toDegrees(angle + trapPhysics.initialRotation));
        final Vector2 newPosition = Constants.ConvertRadialTo2DPosition(
                trapPhysics.startingRadialPosition + trapRingPhysics.body.getAngle(), trapPhysics.onOutsideEdge);
        newPosition.scl(Constants.METERS_TO_PIXELS);
        render.setPosition(newPosition);
    }
}
