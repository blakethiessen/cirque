package com.oak.projectoak.physics.contactlisteners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

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

    public void clearContactListeners()
    {
        contactListeners.clear();
    }

    @Override
    public void beginContact(Contact contact)
    {
        for (BaseContactListener contactListener : contactListeners)
        {
            if (contactListener.beginContact(contact))
                return;
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        for (BaseContactListener contactListener : contactListeners)
        {
            if (contactListener.endContact(contact))
                return;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}
