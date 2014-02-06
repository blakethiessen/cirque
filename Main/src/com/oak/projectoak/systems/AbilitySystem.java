package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.components.Ability;

public class AbilitySystem extends EntityProcessingSystem
{

    public AbilitySystem()
    {
        super(Aspect.getAspectForAll(Ability.class));
    }

    @Override
    protected void process(Entity e)
    {

    }
}
