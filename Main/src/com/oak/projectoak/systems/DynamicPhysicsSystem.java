package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
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
        Render draw = dm.get(e);
        Vector2 position = dpm.get(e).body.getPosition();

        draw.x = Constants.ConvertMetersToPixels(position.x);
        draw.y = Constants.ConvertMetersToPixels(position.y);

//        DEBUGDISPLAY CODE
        Vector2 velocity = dpm.get(e).body.getLinearVelocity();
        float mass = dpm.get(e).body.getMass();
        DebugDisplay.addLine("Player pos(px): " + Math.floor(draw.x) + ", " + Math.floor(draw.y));
        DebugDisplay.addLine("Player pos(m): " + Math.floor(position.x) + ", " + Math.floor(position.y));
        DebugDisplay.addLine("Player vel(m): " + Math.floor(velocity.x) + ", " + Math.floor(velocity.y));
        DebugDisplay.addLine("Player mass: " + mass);
//        DEBUGDISPLAY CODE END
    }
}
