package com.blakeandshahan.cirque.systems.ability;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.blakeandshahan.cirque.Action;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.*;
import com.blakeandshahan.cirque.components.physics.ArenaTransform;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;

public class AbilityCreationSystem extends IteratingSystem
{
    private final AbilityDestructionSystem abilityDestructionSystem;
    private final GameModeManager gmManager;
    private final Body trapRingBody;

    public AbilityCreationSystem(AbilityDestructionSystem abilityDestructionSystem, GameModeManager gmManager, Entity trapRing)
    {
        super(Family.getFor(Player.class));

        this.abilityDestructionSystem = abilityDestructionSystem;
        this.gmManager = gmManager;
        trapRingBody = trapRing.getComponent(DynamicPhysics.class).body;
    }

    @Override
    public boolean checkProcessing()
    {
        return !gmManager.isGameOver();
    }

    @Override
    protected void processEntity(final Entity e, float deltaTime)
    {
        final Player player = Mapper.player.get(e);
        Platformer platformer = Mapper.platformer.get(e);

        AbilityCreation[] abilities = player.abilities;

        for (int i = Action.ABILITY_1.getId(); i <= Action.ABILITY_3.getId(); i++)
        {
            final int adjustedIndex = i - Action.ABILITY_1.getId();

            if (player.isActionOn(i))
            {
                final AbilityCreation curAbility = abilities[adjustedIndex];

                if (!curAbility.justUsed && platformer.isOnGround() && curAbility.enoughEnergyForUse())
                {
                    final ArenaTransform arenaTransform = Mapper.arenaTransform.get(e);
                    Animate animate = Mapper.animate.get(e);
                    final PlayerAnimation playerAnimation = Mapper.playerAnimation.get(e);

                    animate.setAnimation(playerAnimation.layTrap, true);

                    Timer.schedule(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            switch (curAbility.abilityType)
                            {
                                case STAKE:
                                    scheduleEntityForDestruction(EntityFactory.createStake(trapRingBody,
                                            arenaTransform.onOutsideEdge ? arenaTransform.radialPosition -
                                                    Constants.ABILITY_SPAWN_OFFSET :
                                                    arenaTransform.radialPosition + Constants.ABILITY_SPAWN_OFFSET,
                                            !arenaTransform.onOutsideEdge, e), Constants.STAKE_LIFETIME);
                                    break;
                                case PILLAR:
                                    // Pillar destruction is managed in the PillarSystem.
                                    EntityFactory.createPillar(trapRingBody,
                                            arenaTransform.onOutsideEdge ? arenaTransform.radialPosition -
                                                    Constants.ABILITY_SPAWN_OFFSET :
                                                    arenaTransform.radialPosition + Constants.ABILITY_SPAWN_OFFSET,
                                            !arenaTransform.onOutsideEdge);
                                    break;
                                case LIGHTNING_BOLT:
                                    EntityFactory.createLightningBolt(arenaTransform.onOutsideEdge ?
                                                    arenaTransform.radialPosition - Constants.ABILITY_SPAWN_OFFSET :
                                                    arenaTransform.radialPosition -
                                                            Constants.INSIDE_LIGHTNING_SPAWN_OFFSET,
                                            !arenaTransform.onOutsideEdge, e);
                                    break;
                                default:
                                    Gdx.app.error("Invalid ability creation", "No implementation for ability.");
                            }
                        }
                    }, Constants.ABILITY_CREATION_DELAY);

                    curAbility.energyAmt -= curAbility.energyCostPerUse;
                    curAbility.justUsed = true;
                    curAbility.numUsesAvailable--;
                }
            }
            else
                abilities[adjustedIndex].justUsed = false;
        }
    }

    private void scheduleEntityForDestruction(final Entity entity, int entityLifetime)
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                abilityDestructionSystem.destroyEntity(entity);
            }
        }, entityLifetime);
    }
}
