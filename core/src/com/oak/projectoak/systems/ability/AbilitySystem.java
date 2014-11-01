package com.oak.projectoak.systems.ability;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.Ability;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.physics.contactlisteners.BaseContactListener;
import com.oak.projectoak.physics.userdata.ArenaUD;
import com.oak.projectoak.physics.userdata.LethalUD;
import com.oak.projectoak.physics.userdata.LightningUD;
import com.oak.projectoak.physics.userdata.PlayerUD;
import com.oak.projectoak.systems.PlayerDestructionSystem;

public class AbilitySystem extends IteratingSystem
    implements BaseContactListener
{
    private PlayerDestructionSystem playerDestructionSystem;
    private AbilityDestructionSystem abilityDestructionSystem;
    private DeathMatchManager dmManager;

    public AbilitySystem(PlayerDestructionSystem playerDestructionSystem,
                         AbilityDestructionSystem abilityDestructionSystem, DeathMatchManager dmManager)
    {
        super(Family.getFor(Ability.class));
        this.playerDestructionSystem = playerDestructionSystem;
        this.abilityDestructionSystem = abilityDestructionSystem;
        this.dmManager = dmManager;
    }

    @Override
    public boolean checkProcessing()
    {
        return false;
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
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

        if (!Mapper.player.get(entity).invulnerable)
        {
            Player player = entity.getComponent(Player.class);
            dmManager.addKillStatistic(player.teamNum);

            AssetLoader.playSound("death");

            playerDestructionSystem.destroyEntity(entity);
        }

        Player player = Mapper.player.get(Mapper.ability.get((fixtureUDA).entity).owner);
        int OwnerTeamNumber = player.teamNum;
        int VictimTeamNumber = Mapper.player.get((bodyUDB).entity).teamNum;

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
