package com.oak.projectoak.systems.ability;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.components.physics.TrapPhysics;
import com.oak.projectoak.entity.EntityFactory;

import java.util.ArrayList;

public class AbilityDestructionSystem extends EntitySystem
{
    private final World b2world;
    private ArrayList<Entity> entitiesToDestroy;

    private ArrayList<Platformer> externalPlatformers;
    private ArrayList<Platformer> internalPlatformers;

    public AbilityDestructionSystem(World b2world)
    {
        entitiesToDestroy = new ArrayList<Entity>();
        externalPlatformers = new ArrayList<Platformer>(2);
        internalPlatformers = new ArrayList<Platformer>(2);
        this.b2world = b2world;
    }

    public void destroyEntity(Entity entity)
    {
        if (!entitiesToDestroy.contains(entity))
            entitiesToDestroy.add(entity);
    }

    @Override
    public boolean checkProcessing()
    {
        return !entitiesToDestroy.isEmpty();
    }

    @Override
    public void update(float deltaTime)
    {
        while (!entitiesToDestroy.isEmpty())
        {
            removeEntity(entitiesToDestroy.get(0));
        }
    }

    private void removeEntity(final Entity e)
    {
        if (Mapper.physics.has(e))
            b2world.destroyBody(Mapper.physics.get(e).body);
        else if (Mapper.dynamicPhysics.has(e))
            b2world.destroyBody(Mapper.dynamicPhysics.get(e).body);
        else if (Mapper.trapPhysics.has(e))
        {
            TrapPhysics trapPhysics = Mapper.trapPhysics.get(e);
            Fixture fixtureToRemove = trapPhysics.fixture;
            fixtureToRemove.getBody().destroyFixture(fixtureToRemove);

            if (trapPhysics.onOutsideEdge)
                updateRelevantFootContacts(fixtureToRemove, externalPlatformers);
            else
                updateRelevantFootContacts(fixtureToRemove, internalPlatformers);
        }

        EntityFactory.engine.removeEntity(e);
        entitiesToDestroy.remove(e);
    }

    private void updateRelevantFootContacts(Fixture fixtureToRemove, ArrayList<Platformer> platformers)
    {
        for (Platformer platformer : platformers)
        {
            for (int i = 0; i < platformer.footContacts.size(); i++)
            {
                final ArrayList<Fixture> footContacts = platformer.footContacts;
                if (footContacts.get(i).equals(fixtureToRemove))
                {
                    footContacts.remove(i);
                    return;
                }
            }
        }
    }

    public void addFootContactUser(Platformer platformer, boolean onOutsideEdge)
    {
        if (onOutsideEdge)
            externalPlatformers.add(platformer);
        else
            internalPlatformers.add(platformer);
    }
}
