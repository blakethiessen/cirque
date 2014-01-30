package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.External;

public class ExternalGravitySystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Render> dm;

    private final Vector2 arenaCenter;

    public ExternalGravitySystem(Vector2 arenaCenter)
    {
        super(Aspect.getAspectForAll(External.class, DynamicPhysics.class, Render.class));
        this.arenaCenter = arenaCenter;
    }

    @Override
    protected void process(Entity e)
    {
        Render render = dm.get(e);
        DynamicPhysics physics = dpm.get(e);

        Body body = physics.body;
        Vector2 position = body.getPosition();

        // Apply radial gravitational force
        Vector2 curGravityVector = arenaCenter.cpy();
        curGravityVector.sub(position);
        body.applyForceToCenter(curGravityVector, true);

        physics.curGravityVec = curGravityVector;

        // Set the player's feet towards the center of the circle.
        double rotation = Math.atan2(curGravityVector.x, -curGravityVector.y);
        body.setTransform(position, (float)rotation);
        render.rotation = (float)Math.toDegrees(rotation);
    }
}
