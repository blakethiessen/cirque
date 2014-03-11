package com.oak.projectoak.systems.ability;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.components.physics.TrapPhysics;

import java.util.ArrayList;

public class AbilityDestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<Physics> pm;
    @Mapper ComponentMapper<TrapPhysics> tpm;

    private final World b2world;
    private ArrayList<Entity> entitiesToDestroy;

    private ArrayList<Platformer> externalPlatformer;
    private ArrayList<Platformer> internalPlatformer;

    public AbilityDestructionSystem(World b2world)
    {
        entitiesToDestroy = new ArrayList<Entity>();
        externalPlatformer = new ArrayList<Platformer>(2);
        internalPlatformer = new ArrayList<Platformer>(2);
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

            final TrapPhysics trapPhysics = tpm.get(e);
            Fixture fixtureToRemove = trapPhysics.fixture;

            if (pm.has(e))
                b2world.destroyBody(pm.get(e).body);
            else if (tpm.has(e))
            {
                fixtureToRemove.getBody().destroyFixture(fixtureToRemove);
            }

            if (trapPhysics.onOutsideEdge)
                updateRelevantFootContacts(fixtureToRemove, externalPlatformer);
            else
                updateRelevantFootContacts(fixtureToRemove, internalPlatformer);

            world.deleteEntity(e);
            entitiesToDestroy.remove(e);
        }
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
            externalPlatformer.add(platformer);
        else
            internalPlatformer.add(platformer);
    }
}
