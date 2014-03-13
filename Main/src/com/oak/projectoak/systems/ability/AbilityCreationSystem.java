package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.oak.projectoak.Action;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.entity.EntityFactory;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> playm;
    @Mapper ComponentMapper<Platformer> platm;
    @Mapper ComponentMapper<ArenaTransform> cpm;
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<PlayerAnimation> pam;

    private final AbilityDestructionSystem abilityDestructionSystem;
    private final Body trapRingBody;

    public AbilityCreationSystem(AbilityDestructionSystem abilityDestructionSystem, Entity trapRing)
    {
        super(Aspect.getAspectForAll(Player.class));

        this.abilityDestructionSystem = abilityDestructionSystem;
        trapRingBody = trapRing.getComponent(DynamicPhysics.class).body;
    }

    @Override
    protected void process(Entity e)
    {
        final Player player = playm.get(e);
        Platformer platformer = platm.get(e);

        AbilityCreation[] abilities = player.abilities;

        for (int i = Action.ABILITY_1.getId(); i <= Action.ABILITY_3.getId(); i++)
        {
            final int adjustedIndex = i - Action.ABILITY_1.getId();

            if (player.isActionOn(i))
            {
                final AbilityCreation curAbility = abilities[adjustedIndex];

                if (!curAbility.justUsed && platformer.isOnGround() && curAbility.enoughEnergyForUse())
                {
                    final ArenaTransform arenaTransform = cpm.get(e);
                    Animate animate = am.get(e);
                    final PlayerAnimation playerAnimation = pam.get(e);

                    animate.setAnimation(playerAnimation.layTrap, true);

                    Timer.schedule(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            switch (curAbility.abilityType)
                            {
                                case STAKE:
                                    scheduleEntityForDestruction(EntityFactory.createStake(world, trapRingBody,
                                            arenaTransform.radialPosition,
                                            !arenaTransform.onOutsideEdge), Constants.STAKE_LIFETIME);
                                    break;
                                case PILLAR:
                                    scheduleEntityForDestruction(EntityFactory.createPillar(world, trapRingBody,
                                            arenaTransform.radialPosition,
                                            !arenaTransform.onOutsideEdge), Constants.PILLAR_LIFETIME);
                                    break;
                                default:
                                    Gdx.app.error("Invalid ability creation", "No implementation for ability.");
                            }
                        }
                    }, Constants.ABILITY_CREATION_DELAY);

                    curAbility.energyAmt -= curAbility.energyCostPerUse;
                    curAbility.justUsed = true;
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
