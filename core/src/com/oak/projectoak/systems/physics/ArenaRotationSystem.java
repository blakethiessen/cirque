package com.oak.projectoak.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.ArenaRotation;
import com.oak.projectoak.components.physics.DynamicPhysics;

public class ArenaRotationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<DynamicPhysics> pm;
    @Mapper ComponentMapper<ArenaRotation> am;

    private boolean increaseRotationalVelocity;

    public ArenaRotationSystem()
    {
        super(Aspect.getAspectForAll(ArenaRotation.class));
        increaseRotationalVelocity = false;
    }

    @Override
    public boolean checkProcessing()
    {
        return increaseRotationalVelocity;
    }

    @Override
    protected void process(Entity e)
    {
        DynamicPhysics physics = pm.get(e);
        ArenaRotation arenaRotation = am.get(e);

        arenaRotation.rotationVelocity += Constants.ROTATIONAL_VELOCITY_INCREASE_PER_KILL;
        physics.body.setAngularVelocity(am.get(e).rotationVelocity);
    }

    @Override
    protected void end()
    {
        increaseRotationalVelocity = false;
    }

    public void increaseArenaRotationalVelocity()
    {
        increaseRotationalVelocity = true;
    }
}