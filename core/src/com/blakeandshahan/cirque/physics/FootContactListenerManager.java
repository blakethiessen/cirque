package com.blakeandshahan.cirque.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;
import com.blakeandshahan.cirque.physics.contactlisteners.BaseContactListener;
import com.blakeandshahan.cirque.physics.userdata.UserData;

import java.util.HashSet;

public class FootContactListenerManager implements BaseContactListener, EntityListener
{
    private HashSet<Entity> footContactEntities;

    public FootContactListenerManager()
    {
        footContactEntities = new HashSet<Entity>();
    }

    @Override
    public void entityAdded(Entity entity)
    {
        footContactEntities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity)
    {
        footContactEntities.remove(entity);
    }

    @Override
    public boolean beginContact(Contact contact)
    {
        UserData udA = (UserData)contact.getFixtureA().getUserData();
        UserData udB = (UserData)contact.getFixtureB().getUserData();

        for (Entity e : footContactEntities)
        {
            DynamicPhysics p = Mapper.dynamicPhysics.get(e);
            if (p != null && p.body != null && p.body.getFixtureList().size > 1)
            {
                final Fixture footFixture = p.body.getFixtureList().get(1);

                if (udA != null && footFixture.getUserData() == udA)
                {
                    Mapper.platformer.get(e).footContacts.add(contact.getFixtureB());

                    return true;
                }
                else if ((udB != null && footFixture.getUserData() == udB))
                {
                    Mapper.platformer.get(e).footContacts.add(contact.getFixtureA());

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
                DynamicPhysics p = Mapper.dynamicPhysics.get(e);

                // Remove entity on the next update.
                if (p != null && p.body != null && p.body.getFixtureList().size > 1)
                {
                    final Fixture footFixture = p.body.getFixtureList().get(1);

                    if (udA != null && footFixture.getUserData() == udA)
                    {
                        Mapper.platformer.get(e).footContacts.remove(contact.getFixtureB());

                        return true;
                    }
                    else if ((udB != null && footFixture.getUserData() == udB))
                    {
                        Mapper.platformer.get(e).footContacts.remove(contact.getFixtureA());

                        return true;
                    }
                }
            }
        }

        return false;
    }
}
