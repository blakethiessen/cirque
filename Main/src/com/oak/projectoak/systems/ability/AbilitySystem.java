package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.components.ArenaRotation;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.LethalUD;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.systems.PlayerDestructionSystem;

public class AbilitySystem extends EntityProcessingSystem
    implements BaseContactListener
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<ArenaRotation> am;

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
        final Object fixtureUDA = contact.getFixtureA().getUserData();
        final Object bodyUDB = contact.getFixtureB().getBody().getUserData();

        if (fixtureUDA instanceof LethalUD)
        {
            if (bodyUDB instanceof PlayerUD)
            {
                killPlayer((PlayerUD)bodyUDB);

                return true;
            }
        }
        else
        {
            final Object fixtureUDB = contact.getFixtureB().getUserData();
            final Object bodyUDA = contact.getFixtureA().getBody().getUserData();

            if (fixtureUDB instanceof LethalUD)
            {
                if (bodyUDA instanceof PlayerUD)
                {
                    killPlayer((PlayerUD)bodyUDA);

                    return true;
                }
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
