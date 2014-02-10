package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.oak.projectoak.Action;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.physics.CirclePosition;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.physics.userdata.TrapUD;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<CirclePosition> cpm;

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
        CirclePosition circlePosition = cpm.get(e);

        if (player.isActionOn(Action.ABILITY_1) && player.energyAmt >= 0.25f)
        {
            boolean trapInside = false;
            b2world.QueryAABB(new QueryCallback() {
                @Override
                public boolean reportFixture(Fixture fixture) {
                    if (fixture.getBody().getUserData() instanceof TrapUD)
                    {
//                        trapInside = true;
                    }
                }
            });
            EntityFactory.createStake(world,
                    circlePosition.radialPosition, -circlePosition.distanceFromEdge);
            player.energyAmt -= .25f;
        }
    }
}
