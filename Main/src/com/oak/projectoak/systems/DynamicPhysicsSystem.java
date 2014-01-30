package com.oak.projectoak.systems;

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
import com.oak.projectoak.utils.DebugDisplay;

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

        Body body = physics.body;
        Vector2 position = body.getPosition();

        render.x = Constants.ConvertMetersToPixels(position.x);
        render.y = Constants.ConvertMetersToPixels(position.y);

//        DEBUGDISPLAY CODE
        Vector2 velocity = body.getLinearVelocity();
        float mass = body.getMass();
        DebugDisplay.addLine("Player pos(px): " + Math.floor(render.x) + ", " + Math.floor(render.y));
        DebugDisplay.addLine("Player pos(m): " + Math.floor(position.x) + ", " + Math.floor(position.y));
        DebugDisplay.addLine("Player vel(m): " + Math.floor(velocity.x) + ", " + Math.floor(velocity.y));
        DebugDisplay.addLine("Player mass: " + mass);
//        DEBUGDISPLAY CODE END
    }
}
