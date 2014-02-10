package com.oak.projectoak.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;

import java.util.ArrayList;

public class DestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<Physics> pm;
    @Mapper ComponentMapper<DynamicPhysics> dpm;

    private ArrayList<Entity> entitiesToDestroy;
    private final World b2world;
    private FootContactListenerSystem footContactListenerSystem;

    public DestructionSystem(World b2world, FootContactListenerSystem footContactListenerSystem)
    {
        this.footContactListenerSystem = footContactListenerSystem;
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
            else if (dpm.has(e))
                b2world.destroyBody(dpm.get(e).body);

            e.deleteFromWorld();
            entitiesToDestroy.remove(e);

            footContactListenerSystem.removeIdenticalEntity(e);
        }
    }
}
