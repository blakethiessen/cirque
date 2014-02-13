package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.components.Arena;
import com.oak.projectoak.components.physics.Physics;

public class ArenaRotationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Physics> pm;

    public ArenaRotationSystem()
    {
        super(Aspect.getAspectForAll(Arena.class));
    }

    @Override
    protected void process(Entity e)
    {
        Physics physics = pm.get(e);

        physics.body.setAngularVelocity(.1f);
    }
}
