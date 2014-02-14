package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.physics.userdata.TrapUD;
import gamemodemanagers.DeathMatchManager;

public class AbilitySystem extends EntityProcessingSystem
    implements BaseContactListener
{
    private World b2world;
    private DestructionSystem destructionSystem;
    private DeathMatchManager dmManager;

    public AbilitySystem(World b2world, DestructionSystem destructionSystem, DeathMatchManager dmManager)
    {
        super(Aspect.getAspectForAll(Ability.class));
        this.b2world = b2world;
        this.destructionSystem = destructionSystem;
        this.dmManager = dmManager;
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
                return killPlayer((PlayerUD) userDataB);
            }
        }
        else if (userDataB instanceof TrapUD)
        {
            if (userDataA instanceof PlayerUD)
            {
                return killPlayer((PlayerUD) userDataA);
            }
        }
        return false;
    }

    private boolean killPlayer(PlayerUD userData)
    {
        final Entity entity = userData.entity;

        dmManager.addDeath(entity.getComponent(Player.class).teamNum);
        destructionSystem.destroyEntity(entity);

        return true;
    }

    @Override
    public boolean endContact(Contact contact)
    {
        return false;
    }
}
