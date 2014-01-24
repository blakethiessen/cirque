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

    private Vector2 arenaCenter;

    public DynamicPhysicsSystem(Vector2 arenaCenter)
    {
        super(Aspect.getAspectForAll(DynamicPhysics.class, Render.class));
        this.arenaCenter = arenaCenter;
    }

    @Override
    protected void process(Entity e)
    {
        Render draw = dm.get(e);
        Body body = dpm.get(e).body;
        Vector2 position = body.getPosition();

        draw.x = Constants.ConvertMetersToPixels(position.x);
        draw.y = Constants.ConvertMetersToPixels(position.y);

        // Apply radial gravitational force
        Vector2 toArenaCenter = new Vector2(0, 0);
        toArenaCenter.add(arenaCenter);
        toArenaCenter.sub(position);
        body.applyForceToCenter(toArenaCenter, true);

        // Set the player's feet towards the center of the circle.
        body.setTransform(position, (float)Math.atan2(toArenaCenter.x, -toArenaCenter.y));

//        DEBUGDISPLAY CODE
        Vector2 velocity = body.getLinearVelocity();
        float mass = body.getMass();
        DebugDisplay.addLine("Player pos(px): " + Math.floor(draw.x) + ", " + Math.floor(draw.y));
        DebugDisplay.addLine("Player pos(m): " + Math.floor(position.x) + ", " + Math.floor(position.y));
        DebugDisplay.addLine("Player vel(m): " + Math.floor(velocity.x) + ", " + Math.floor(velocity.y));
        DebugDisplay.addLine("Player mass: " + mass);
//        DEBUGDISPLAY CODE END
    }
}
