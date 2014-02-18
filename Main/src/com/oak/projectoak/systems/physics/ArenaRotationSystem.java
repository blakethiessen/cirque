package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Arena;
import com.oak.projectoak.components.physics.Physics;

public class ArenaRotationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Physics> pm;
    @Mapper ComponentMapper<Arena> am;

    private boolean increaseRotationalVelocity;

    public ArenaRotationSystem()
    {
        super(Aspect.getAspectForAll(Arena.class));
        increaseRotationalVelocity = false;
    }

    @Override
    protected void process(Entity e)
    {
        Physics physics = pm.get(e);
        Arena arena = am.get(e);

        if (increaseRotationalVelocity)
        {
            arena.rotationVelocity += Constants.ROTATIONAL_VELOCITY_INCREASE_PER_KILL;
            increaseRotationalVelocity = false;
        }

        physics.body.setAngularVelocity(am.get(e).rotationVelocity);
    }

    public void increaseArenaRotationalVelocity()
    {
        increaseRotationalVelocity = true;
    }
}
