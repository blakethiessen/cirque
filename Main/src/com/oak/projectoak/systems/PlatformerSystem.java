package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.*;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.physics.userdata.UserData;

import java.util.HashSet;
import java.util.Set;

public class PlatformerSystem extends EntitySystem implements ContactListener
{
    @Mapper ComponentMapper<Platformer> plm;
    @Mapper ComponentMapper<DynamicPhysics> phm;

    private Set<Entity> footContactEntities = new HashSet<Entity>();

    public PlatformerSystem()
    {
        super(Aspect.getAspectForAll(Platformer.class, DynamicPhysics.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entityImmutableBag)
    {
        for (int i = 0; i < entityImmutableBag.size(); i++)
        {
            footContactEntities.add(entityImmutableBag.get(i));
        }
    }

    @Override
    protected boolean checkProcessing()
    {
        //TODO: Set this to true only when new entities are added (that have foot contacts?)
        return true;
    }

    @Override
    public void beginContact(Contact contact)
    {
        UserData udA = (UserData)contact.getFixtureA().getUserData();
        UserData udB = (UserData)contact.getFixtureB().getUserData();
        // TODO: Figure out if every body/fixture should have userData.
        // Then throw exception here instead of null check.
        for (Entity e : footContactEntities)
        {
            DynamicPhysics p = phm.get(e);
            final Fixture footFixture = p.body.getFixtureList().get(1);
            if ((udA != null && footFixture.getUserData() == udA) ||
                (udB != null && footFixture.getUserData() == udB))
            {
                Platformer plat = plm.get(e);
                plat.footContactCount++;
            }
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA != null && fixtureB != null)
        {
            Object udA = fixtureA.getUserData();
            Object udB = fixtureB.getUserData();
            for (Entity e : footContactEntities)
            {
                DynamicPhysics p = phm.get(e);
                final Fixture footFixture = p.body.getFixtureList().get(1);
                if ((udA != null && footFixture.getUserData() == udA) ||
                    (udB != null && footFixture.getUserData() == udB))
                {
                    Platformer plat = plm.get(e);
                    plat.footContactCount--;
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}
