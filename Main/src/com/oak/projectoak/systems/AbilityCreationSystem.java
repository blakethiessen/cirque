package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.Action;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.physics.CirclePosition;
import com.oak.projectoak.entity.EntityFactory;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<CirclePosition> cpm;

    private World world;

    public AbilityCreationSystem(World world)
    {
        super(Aspect.getAspectForAll(Player.class));

        this.world = world;
    }

    @Override
    protected void process(Entity e)
    {
        Player player = pm.get(e);
        CirclePosition circlePosition = cpm.get(e);

        if (player.isActionOn(Action.ABILITY_1))
        {
            System.out.println(circlePosition.radialPosition);
            EntityFactory.createStake(world,
                    circlePosition.radialPosition, -circlePosition.distanceFromEdge);
        }
    }
}
