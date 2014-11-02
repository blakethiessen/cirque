package com.blakeandshahan.cirque.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.ArenaRotation;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;

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
