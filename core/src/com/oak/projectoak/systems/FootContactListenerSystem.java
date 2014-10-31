package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.UserData;

import java.util.HashSet;

public class FootContactListenerSystem extends EntityProcessingSystem
        implements BaseContactListener
{
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Platformer> plm;

    private HashSet<Entity> footContactEntities;
    private int previousActiveEntityCount;

    public FootContactListenerSystem()
    {
        super(Aspect.getAspectForAll(Platformer.class, DynamicPhysics.class));

        footContactEntities = new HashSet<Entity>();
        previousActiveEntityCount = 0;
    }

    @Override
    protected void process(Entity e)
    {
        footContactEntities.add(e);
    }

    @Override
    protected boolean checkProcessing()
    {
        final int activeEntityCount = world.getEntityManager().getActiveEntityCount();
        if (activeEntityCount != previousActiveEntityCount)
        {
            previousActiveEntityCount = activeEntityCount;
            return true;
        }

        return false;
    }

    @Override
    public boolean beginContact(Contact contact)
    {
        UserData udA = (UserData)contact.getFixtureA().getUserData();
        UserData udB = (UserData)contact.getFixtureB().getUserData();

        for (Entity e : footContactEntities)
        {
            DynamicPhysics p = dpm.get(e);
            if (p != null && p.body != null && p.body.getFixtureList().size > 1)
            {
                final Fixture footFixture = p.body.getFixtureList().get(1);
                Platformer plat = plm.get(e);

                if (udA != null && footFixture.getUserData() == udA)
                {
                    plat.footContacts.add(contact.getFixtureB());

                    return true;
                }
                else if ((udB != null && footFixture.getUserData() == udB))
                {
                    plat.footContacts.add(contact.getFixtureA());

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA != null && fixtureB != null)
        {
            Object udA = fixtureA.getUserData();
            Object udB = fixtureB.getUserData();
            for (Entity e : footContactEntities)
            {
                DynamicPhysics p = dpm.get(e);

                // Remove entity on the next update.
                if (p != null && p.body != null && p.body.getFixtureList().size > 1)
                {
                    final Fixture footFixture = p.body.getFixtureList().get(1);
                    Platformer plat = plm.get(e);

                    if (udA != null && footFixture.getUserData() == udA)
                    {
                        plat.footContacts.remove(contact.getFixtureB());

                        return true;
                    }
                    else if ((udB != null && footFixture.getUserData() == udB))
                    {
                        plat.footContacts.remove(contact.getFixtureA());

                        return true;
                    }
                }
            }
        }

        return false;
    }
}