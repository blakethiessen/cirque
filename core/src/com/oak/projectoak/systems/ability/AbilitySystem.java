package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.ArenaUD;
import com.oak.projectoak.physics.userdata.LethalUD;
import com.oak.projectoak.physics.userdata.LightningUD;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.systems.PlayerDestructionSystem;

public class AbilitySystem extends EntityProcessingSystem
    implements BaseContactListener
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<Ability> abm;

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
                return killPlayer((LethalUD) fixtureUDA, (PlayerUD) bodyUDB);
            }
            else if (fixtureUDA instanceof LightningUD && bodyUDB instanceof ArenaUD)
            {
                abilityDestructionSystem.destroyEntity(((LightningUD) fixtureUDA).entity);
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
                    return killPlayer((LethalUD) fixtureUDB, (PlayerUD) bodyUDA);
                }
                else if (fixtureUDB instanceof LightningUD && bodyUDA instanceof ArenaUD)
                {
                    abilityDestructionSystem.destroyEntity(((LightningUD) fixtureUDB).entity);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean killPlayer(LethalUD fixtureUDA, PlayerUD bodyUDB)
    {
        Entity entity = (bodyUDB).entity;

        if (!pm.get(entity).invulnerable)
        {
            Player player = entity.getComponent(Player.class);
            dmManager.addKillStatistic(player.teamNum);

            AssetLoader.playSound("death");

            playerDestructionSystem.destroyEntity(entity);
        }

        Player player = pm.get(abm.get((fixtureUDA).entity).owner);
        int OwnerTeamNumber = player.teamNum;
        int VictimTeamNumber = pm.get((bodyUDB).entity).teamNum;

        if(OwnerTeamNumber == VictimTeamNumber)
            player.friendlyKills++;
        else
            player.enemyKills++;

        return true;
    }

    @Override
    public boolean endContact(Contact contact)
    {
        return false;
    }
}
