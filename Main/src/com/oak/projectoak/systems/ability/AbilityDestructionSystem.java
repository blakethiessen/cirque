package com.oak.projectoak.systems.ability;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.Timer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.Pillar;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.components.physics.TrapPhysics;

import java.util.ArrayList;

public class AbilityDestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<Physics> pm;
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<TrapPhysics> tpm;

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
    protected boolean checkProcessing()
    {
        return !entitiesToDestroy.isEmpty();
    }

    @Override
    protected void processSystem()
    {
        while (!entitiesToDestroy.isEmpty())
        {
            removeEntity(entitiesToDestroy.get(0));
        }
    }

    private void removeEntity(final Entity e)
    {
        if (pm.has(e))
            b2world.destroyBody(pm.get(e).body);
        else if (dpm.has(e))
            b2world.destroyBody(dpm.get(e).body);
        else if (tpm.has(e))
        {
            TrapPhysics trapPhysics = tpm.get(e);
            Fixture fixtureToRemove = trapPhysics.fixture;
            fixtureToRemove.getBody().destroyFixture(fixtureToRemove);

            if (trapPhysics.onOutsideEdge)
                updateRelevantFootContacts(fixtureToRemove, externalPlatformers);
            else
                updateRelevantFootContacts(fixtureToRemove, internalPlatformers);
        }

        world.deleteEntity(e);
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
