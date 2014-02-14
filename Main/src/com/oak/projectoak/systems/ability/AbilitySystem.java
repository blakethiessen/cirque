package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.physics.userdata.TrapUD;
import com.oak.projectoak.systems.PlayerDestructionSystem;
import gamemodemanagers.DeathMatchManager;

public class AbilitySystem extends EntityProcessingSystem
    implements BaseContactListener
{
    @Mapper ComponentMapper<Player> pm;

    private PlayerDestructionSystem playerDestructionSystem;
    private AbilityDestructionSystem abilityDestructionSystem;
    private DeathMatchManager dmManager;

    public AbilitySystem(PlayerDestructionSystem playerDestructionSystem,
                         AbilityDestructionSystem abilityDestructionSystem, DeathMatchManager dmManager)
    {
        super(Aspect.getAspectForAll(Ability.class));
        this.playerDestructionSystem = playerDestructionSystem;
        this.abilityDestructionSystem = abilityDestructionSystem;
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
                killPlayer((PlayerUD) userDataB);

                return true;
            }
        }
        else if (userDataB instanceof TrapUD)
        {
            if (userDataA instanceof PlayerUD)
            {
                killPlayer((PlayerUD)userDataA);

                return true;
            }
        }
        return false;
    }

    private void killPlayer(PlayerUD userData)
    {
        Entity entity = userData.entity;

        if (!pm.get(entity).invulnerable)
        {
            dmManager.addKillStatistic(entity.getComponent(Player.class).teamNum);
            playerDestructionSystem.destroyEntity(entity);
        }
    }

    @Override
    public boolean endContact(Contact contact)
    {
        return false;
    }
}
