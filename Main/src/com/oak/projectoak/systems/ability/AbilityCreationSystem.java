package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.oak.projectoak.Action;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.physics.FixturePositioningBody;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> playm;
    @Mapper ComponentMapper<Platformer> platm;
    @Mapper ComponentMapper<ArenaTransform> cpm;
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<PlayerAnimation> pam;

    private World world;

    private final AbilityDestructionSystem abilityDestructionSystem;
    private final FixturePositioningBody trapRingBody;

    public AbilityCreationSystem(World world, AbilityDestructionSystem abilityDestructionSystem, Entity trapRing)
    {
        super(Aspect.getAspectForAll(Player.class));

        this.world = world;
        this.abilityDestructionSystem = abilityDestructionSystem;
        trapRingBody = new FixturePositioningBody(trapRing.getComponent(DynamicPhysics.class).body);
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
                                            !arenaTransform.onOutsideEdge));
                                    break;
                                case PILLAR:
                                    // TODO
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

    private void scheduleEntityForDestruction(final Entity entity)
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                abilityDestructionSystem.destroyEntity(entity);
            }
        }, Constants.STAKE_LIFETIME);
    }
}
