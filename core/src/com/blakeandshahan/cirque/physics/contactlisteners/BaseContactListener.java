package com.blakeandshahan.cirque.physics.contactlisteners;

import com.badlogic.gdx.physics.box2d.Contact;

public interface BaseContactListener
{
    public boolean beginContact(Contact contact);
    public boolean endContact(Contact contact);
}
