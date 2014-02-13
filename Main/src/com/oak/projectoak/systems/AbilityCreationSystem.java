package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.Action;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.PlayerAnimation;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.entity.EntityFactory;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<ArenaTransform> cpm;
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<PlayerAnimation> pam;
    @Mapper ComponentMapper<DynamicPhysics> dpm;

    private World world;
    private final com.badlogic.gdx.physics.box2d.World b2world;

    public AbilityCreationSystem(World world, com.badlogic.gdx.physics.box2d.World b2world)
    {
        super(Aspect.getAspectForAll(Player.class));

        this.world = world;
        this.b2world = b2world;
    }

    @Override
    protected void process(Entity e)
    {
        Player player = pm.get(e);

        if (player.isActionOn(Action.ABILITY_1) && player.energyAmt >= 0.25f)
        {
            ArenaTransform arenaTransform = cpm.get(e);
            Animate animate = am.get(e);
            PlayerAnimation playerAnimation = pam.get(e);
            DynamicPhysics dynamicPhysics = dpm.get(e);

            animate.setAnimation(playerAnimation.layTrap, true);

            EntityFactory.createStake(world, arenaTransform.radialPosition,
                    !arenaTransform.onOutsideEdge,
                    (float)(dynamicPhysics.body.getAngle() + Math.PI));

            player.energyAmt -= .25f;

            player.setAction(Action.ABILITY_1, false);
        }
    }

}
