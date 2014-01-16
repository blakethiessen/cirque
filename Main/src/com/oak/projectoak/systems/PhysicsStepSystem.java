package com.oak.projectoak.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.Constants;

/*
    The PhysicsStepSystem keeps the physics for
    all entities running.
 */

public class PhysicsStepSystem extends VoidEntitySystem
{
    World b2world;

    public PhysicsStepSystem(World b2world)
    {
        this.b2world = b2world;
    }

    @Override
    protected void processSystem()
    {
        b2world.step(1/60f, 6, 2);
    }

}
