package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Action;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.physics.DynamicPhysics;

import java.util.TimerTask;

/*
    The PlayerMovementSystem moves players when the Player
    component is updated with the latest player actions.
 */

public class PlayerMovementSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Platformer> pm;
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Render> rm;
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<Player> cm;

    public PlayerMovementSystem()
    {
        super(Aspect.getAspectForAll(Player.class, Player.class));
    }

    @Override
    protected void process(Entity e)
    {
        // The following assumes runners always have the following components.
        Player player = cm.get(e);
        DynamicPhysics physics = dpm.get(e);
        Body body = physics.body;
        final Platformer platformer = pm.get(e);
        Render render = rm.get(e);
        Animate animate = am.get(e);

        if (player.isActionOn(Action.MOVING_LEFT))
        {
            // Flip the sprite
            render.flipped = true;
            animate.animation = AssetLoader.getAnimation("run"); // TODO: How to make this more efficient?

            moveLaterally(body, platformer, -platformer.latMaxVel);
        }
        else if (player.isActionOn(Action.MOVING_RIGHT))
        {
            render.flipped = false;
            animate.animation = AssetLoader.getAnimation("run"); // TODO: How to make this more efficient?

            moveLaterally(body, platformer, platformer.latMaxVel);
        }
        else
        {
            float xBodyVel = body.getLinearVelocity().x;

            animate.animation = AssetLoader.getAnimation("idle");

            if (xBodyVel != 0)
            {
                if (platformer.latMaxVel < xBodyVel)
                    body.applyForceToCenter(-platformer.latMaxVel, 0, true);
                else
                    body.applyForceToCenter(-xBodyVel, 0, true);
            }
        }

        if (player.isActionOn(Action.JUMPING) && platformer.footContactCount > 0)
            if (platformer.jumpTimeoutOver)
            {
                // Apply jump force in the opposite direction of gravity.
                body.applyForceToCenter(physics.curGravityVec.scl(-platformer.jumpAccel), true);
                platformer.jumpTimeoutOver = false;

                platformer.jumpTimeout.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        platformer.jumpTimeoutOver = true;
                    }
                }, 500);
            }
    }

    private void moveLaterally(Body body, Platformer platformer, float acceleration)
    {
        if (body.getLinearVelocity().len2() < Math.pow(platformer.latMaxVel, 2))
        {
            Vector2 moveVec = new Vector2(acceleration, 0);
            moveVec.rotate((float)Math.toDegrees(body.getAngle()));

            body.applyForceToCenter(moveVec, true);
        }
    }
}
