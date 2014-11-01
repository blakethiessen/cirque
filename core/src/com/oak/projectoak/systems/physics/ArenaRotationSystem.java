package com.oak.projectoak.systems.physics;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.oak.projectoak.Constants;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.ArenaRotation;
import com.oak.projectoak.components.physics.DynamicPhysics;

public class ArenaRotationSystem extends IteratingSystem
{
    private boolean increaseRotationalVelocity;

    public ArenaRotationSystem()
    {
        super(Family.getFor(ArenaRotation.class));
        increaseRotationalVelocity = false;
    }

    @Override
    public boolean checkProcessing()
    {
        return increaseRotationalVelocity;
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        DynamicPhysics physics = Mapper.dynamicPhysics.get(e);
        ArenaRotation arenaRotation = Mapper.arenaRotation.get(e);

        arenaRotation.rotationVelocity += Constants.ROTATIONAL_VELOCITY_INCREASE_PER_KILL;
        physics.body.setAngularVelocity(arenaRotation.rotationVelocity);

        // TODO: Refactor this to be for one circle?
        increaseRotationalVelocity = false;
    }

    public void increaseArenaRotationalVelocity()
    {
        increaseRotationalVelocity = true;
    }
}
