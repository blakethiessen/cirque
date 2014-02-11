package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.physics.userdata.TrapUD;

public class AbilitySystem extends EntityProcessingSystem
    implements BaseContactListener
{
    private World b2world;
    private DestructionSystem destructionSystem;

    public AbilitySystem(World b2world, DestructionSystem destructionSystem)
    {
        super(Aspect.getAspectForAll(Ability.class));
        this.b2world = b2world;
        this.destructionSystem = destructionSystem;
    }

    @Override
    protected boolean checkProcessing()
    {
        return false;
    }

    @Override
    protected void process(Entity e)
    {

    }

    @Override
    public boolean beginContact(Contact contact)
    {
        final Object userDataA = contact.getFixtureA().getBody().getUserData();
        final Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (userDataA instanceof TrapUD)
        {
            if (userDataB instanceof PlayerUD)
            {
                final Entity entity = ((PlayerUD) userDataB).entity;

                destructionSystem.destroyEntity(entity);

                return true;
            }
        }
        else if (userDataB instanceof TrapUD)
        {
            if (userDataA instanceof PlayerUD)
            {
                final Entity entity = ((PlayerUD) userDataA).entity;

                destructionSystem.destroyEntity(entity);

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean endContact(Contact contact)
    {
        return false;
    }
}
