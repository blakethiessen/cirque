package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.blakeandshahan.cirque.Action;
import com.blakeandshahan.cirque.AssetLoader;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.*;
import com.blakeandshahan.cirque.components.physics.ArenaTransform;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;

/*
    The PlayerMovementSystem moves players when the Player
    component is updated with the latest player actions.
 */

public class PlayerMovementSystem extends IteratingSystem
{
    private final GameModeManager gmManager;

    public PlayerMovementSystem(GameModeManager gmManager)
    {
        super(Family.getFor(PlayerAnimation.class));
        this.gmManager = gmManager;
    }

    @Override
    public boolean checkProcessing()
    {
        return !gmManager.isGameOver();
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        // The following assumes runners always have the following components.
        Player player = Mapper.player.get(e);
        AbilityUser abilityUser = Mapper.abilityUser.get(e);
        DynamicPhysics physics = Mapper.dynamicPhysics.get(e);
        Body body = physics.body;
        final Platformer platformer = Mapper.platformer.get(e);
        Render render = Mapper.render.get(e);
        Animate animate = Mapper.animate.get(e);
        PlayerAnimation playerAnimation = Mapper.playerAnimation.get(e);

        final ArenaTransform arenaTransform = Mapper.arenaTransform.get(e);
        if (player.isActionOn(Action.MOVING_LEFT))
        {
            // Flip the sprites
            render.flipped = true;

            if (platformer.isOnGround() && platformer.jumpTimeoutOver)
                animate.setAnimation(playerAnimation.walk);

            moveAlongArenaEdgeWithSpeedLimit(body, platformer.latMaxVel, -platformer.latAccel);
            increaseEnergy(abilityUser, player, arenaTransform);
        }
        else if (player.isActionOn(Action.MOVING_RIGHT))
        {
            render.flipped = false;

            if (platformer.isOnGround() && platformer.jumpTimeoutOver)
                animate.setAnimation(playerAnimation.walk);

            moveAlongArenaEdgeWithSpeedLimit(body, platformer.latMaxVel, platformer.latAccel);
            increaseEnergy(abilityUser, player, arenaTransform);
        }
        else
        {
            // If we're on the floor...
            if (platformer.isOnGround() && platformer.jumpTimeoutOver)
                animate.setAnimation(playerAnimation.idle);

            final float bodyEdgeVelocity = getBodyEdgeVelocity(body);
            if (bodyEdgeVelocity != 0)
            {
                moveAlongArenaEdge(body, -bodyEdgeVelocity);
            }
        }

        if (player.isActionOn(Action.JUMPING) && platformer.isOnGround() && !animate.playWithNoInterrupts)
            if (platformer.jumpTimeoutOver)
            {
                // Apply jump force in the opposite direction of gravity.
                body.applyForceToCenter(physics.curGravityVec.scl(-platformer.jumpAccel), true);
                platformer.jumpTimeoutOver = false;

                animate.setToStaticTexture();
                Constants.setSpriteTexture(render.sprites[0], AssetLoader.getTextureRegion(playerAnimation.jump));

                AssetLoader.playSound("jump");

                Timer.schedule(new Timer.Task()
                {
                    @Override
                    public void run()
                    {
                        platformer.jumpTimeoutOver = true;
                    }
                }, Constants.JUMP_TIMEOUT_DELAY);
            }

        if (getBodyEdgeVelocity(body) < 0 && player.isMovingRight ||
                getBodyEdgeVelocity(body) > 0 && !player.isMovingRight)
        {
            player.energyIncreaseMultiplier = 1;
            player.lastLateralChangePosition = arenaTransform.radialPosition;
            player.isMovingRight = !player.isMovingRight;
        }
    }

    private void increaseEnergy(AbilityUser abilityUser, Player player, ArenaTransform arenaTransform)
    {
//        if (player.isMovingRight && player.lastLateralChangePosition >= arenaTransform.radialPosition)
//            player.energyIncreaseMultiplier *=2;

        if (abilityUser != null)
        {
            for (AbilityCreation ability : abilityUser.abilities)
            {
                if (ability.energyAmt <= 1f)
                    ability.energyAmt += ability.energyIncreasePerFrame * player.energyIncreaseMultiplier;
                else
                    ability.energyAmt = 1f;
            }
        }
    }

    private void moveAlongArenaEdgeWithSpeedLimit(Body body, float latMaxVel, float acceleration)
    {
        if (Math.abs(getBodyEdgeVelocity(body)) < latMaxVel)
        {
            moveAlongArenaEdge(body, acceleration);
        }
    }

    private float getBodyEdgeVelocity(Body body)
    {
        return body.getLinearVelocity().rotate((float)Math.toDegrees(-body.getAngle())).x;
    }

    private void moveAlongArenaEdge(Body body, float acceleration)
    {
        Vector2 moveVec = new Vector2(acceleration, 0);
        moveVec.rotate((float)Math.toDegrees(body.getAngle()));

        body.applyForceToCenter(moveVec, true);
    }
}
