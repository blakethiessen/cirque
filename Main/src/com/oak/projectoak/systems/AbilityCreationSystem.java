package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Action;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.PlayerAnimation;
import com.oak.projectoak.components.physics.CirclePosition;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.physics.querycallbacks.TrapIntersectingQueryCallback;

public class AbilityCreationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<CirclePosition> cpm;
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<PlayerAnimation> pam;

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
            CirclePosition circlePosition = cpm.get(e);
            Animate animate = am.get(e);
            PlayerAnimation playerAnimation = pam.get(e);

            final TrapIntersectingQueryCallback trapCallback = new TrapIntersectingQueryCallback();
            Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(circlePosition.radialPosition, -circlePosition.distanceFromEdge);

            b2world.QueryAABB(trapCallback, twoDPosition.x, twoDPosition.y, twoDPosition.x + 1, twoDPosition.y + 1);

            if (!trapCallback.isTrapIntersecting())
            {
                animate.setAnimation(playerAnimation.layTrap, true);
                EntityFactory.createStake(world,
                        circlePosition.radialPosition, -circlePosition.distanceFromEdge);
                player.energyAmt -= .25f;
            }
        }
    }
}
