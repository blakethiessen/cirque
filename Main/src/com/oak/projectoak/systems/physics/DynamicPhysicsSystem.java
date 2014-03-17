package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.physics.DynamicPhysics;

// Updates draw component position with dynamic physics component position.
public class DynamicPhysicsSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Render> dm;

    public DynamicPhysicsSystem()
    {
        super(Aspect.getAspectForAll(DynamicPhysics.class, Render.class));
    }

    @Override
    protected void process(Entity e)
    {
        Render render = dm.get(e);
        DynamicPhysics physics = dpm.get(e);

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
