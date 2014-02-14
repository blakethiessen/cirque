package com.oak.projectoak.systems.ability;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.physics.Physics;

import java.util.ArrayList;

public class AbilityDestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<Physics> pm;

    private final World b2world;
    private ArrayList<Entity> entitiesToDestroy;

    public AbilityDestructionSystem(World b2world)
    {
        entitiesToDestroy = new ArrayList<Entity>();
        this.b2world = b2world;
    }

    public void destroyEntity(Entity entity)
    {
        entitiesToDestroy.add(entity);
    }

    @Override
    protected boolean checkProcessing()
    {
        return !entitiesToDestroy.isEmpty();
    }

    @Override
    protected void processSystem()
    {
        while (!entitiesToDestroy.isEmpty())
        {
            Entity e = entitiesToDestroy.get(0);

            if (pm.has(e))
                b2world.destroyBody(pm.get(e).body);

            world.deleteEntity(e);
            entitiesToDestroy.remove(e);
        }
    }
}
