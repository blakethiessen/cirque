package com.oak.projectoak.physics.contactlisteners;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.*;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;

import java.util.ArrayList;

public class GFContactListener implements ContactListener
{
    private ArrayList<BaseContactListener> contactListeners;

    public GFContactListener()
    {
        contactListeners = new ArrayList<BaseContactListener>();
    }

    public void addContactListener(BaseContactListener contactListener)
    {
        contactListeners.add(contactListener);
    }

    @Override
    public void beginContact(Contact contact)
    {
        for (BaseContactListener contactListener : contactListeners)
        {
            contactListener.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        for (BaseContactListener contactListener : contactListeners)
        {
            contactListener.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}
