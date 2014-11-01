package com.oak.projectoak.systems.physics;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.Constants;

/*
    The PhysicsStepSystem keeps the physics for
    all entities running.
 */

public class PhysicsStepSystem extends EntitySystem
{
    World b2world;

    public PhysicsStepSystem(World b2world)
    {
        this.b2world = b2world;
    }

    @Override
    public void update(float deltaTime)
    {
        b2world.step(1/60f, 6, 2);
    }

}
