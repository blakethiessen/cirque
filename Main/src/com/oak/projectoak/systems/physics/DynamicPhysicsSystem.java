package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Player;
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

        render.position = position.cpy().scl(Constants.METERS_TO_PIXELS);

//        DEBUGDISPLAY CODE
//        Vector2 velocity = body.getLinearVelocity();
//        float mass = body.getMass();
//        DebugDisplay.addLine("Player " + player.playerNum + " pos(px): " + Math.floor(render.position.x) + ", " + Math.floor(render.position.y));
//        DebugDisplay.addLine("Player " + player.playerNum + " pos(m): " + Math.floor(position.x) + ", " + Math.floor(position.y));
//        DebugDisplay.addLine("Player " + player.playerNum + " vel(m): " + Math.floor(velocity.x) + ", " + Math.floor(velocity.y));
//        DebugDisplay.addLine("Player " + player.playerNum + " mass: " + mass);
//        DebugDisplay.addLine("");
//        DEBUGDISPLAY CODE END
    }
}
