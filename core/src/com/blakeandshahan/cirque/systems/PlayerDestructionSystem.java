package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.*;
import com.blakeandshahan.cirque.components.physics.ArenaTransform;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;
import com.blakeandshahan.cirque.physics.PhysicsFactory;
import com.blakeandshahan.cirque.systems.physics.ArenaRotationSystem;

import java.util.ArrayList;

public class PlayerDestructionSystem extends EntitySystem
{
    private final GameModeManager deathMatchManager;
    private final ArenaRotationSystem arenaRotationSystem;
    private final int respawnTime;
    private final ArrayList<Entity> entitiesToDestroy;
    private final World b2world;
    
    private ArrayList<PlayerToRespawn> deadPlayers;

    public PlayerDestructionSystem(World b2world, GameModeManager gameModeManager, int respawnTime, ArenaRotationSystem arenaRotationSystem)
    {
        this.deathMatchManager = gameModeManager;
        this.arenaRotationSystem = arenaRotationSystem;
        entitiesToDestroy = new ArrayList<Entity>(Constants.MAX_NUM_OF_PLAYERS);
        this.b2world = b2world;
        this.respawnTime = respawnTime;
        
        deadPlayers = new ArrayList<PlayerToRespawn>(Constants.MAX_NUM_OF_PLAYERS);
    }

    public void destroyEntity(Entity entity)
    {
        if (!entitiesToDestroy.contains(entity))
            entitiesToDestroy.add(entity);
    }

    @Override
    public boolean checkProcessing()
    {
        return !entitiesToDestroy.isEmpty();
    }

    @Override
    public void update(float deltaTime)
    {
        while (!entitiesToDestroy.isEmpty())
        {
            final Entity e = entitiesToDestroy.get(0);

            Animate animate = Mapper.animate.get(e);
            animate.setAnimation(Mapper.playerAnimation.get(e).death, true);
            final Player player = Mapper.player.get(e);
            player.invulnerable = true;
            player.deaths++;
            player.portraitRender.setNewSpriteImage(player.portraitPortrait.deathPortrait, 1);

            Timer.schedule(new Timer.Task()
            {
                @Override
                public void run()
                {
                    if (Mapper.dynamicPhysics.has(e))
                        b2world.destroyBody(Mapper.dynamicPhysics.get(e).body);

                    Render render = (Render)e.remove(Render.class);
                    PlayerAnimation playerAnimation = (PlayerAnimation)e.remove(PlayerAnimation.class);

                    if (!deathMatchManager.isGameOver())
                        setupPlayerRespawnTimer(new PlayerToRespawn(e, render, playerAnimation));
                    else
                        deadPlayers.add(new PlayerToRespawn(e, render, playerAnimation));

                    arenaRotationSystem.increaseArenaRotationalVelocity();
                }
            }, .6f);

            entitiesToDestroy.remove(e);
        }
    }

    private void setupPlayerRespawnTimer(final PlayerToRespawn playerToRespawn)
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                Body runnerBody = PhysicsFactory.createRunnerBody(playerToRespawn.e);
                ArenaTransform arenaTransform = Mapper.arenaTransform.get(playerToRespawn.e);
                runnerBody.setTransform(Constants.ConvertRadialTo2DPosition(
                        (float) (arenaTransform.radialPosition + Math.PI), arenaTransform.onOutsideEdge), 0);

                Mapper.dynamicPhysics.get(playerToRespawn.e).body = runnerBody;
                Mapper.platformer.get(playerToRespawn.e).footContacts.clear();
                final PlayerController controller = Mapper.playerController.get(playerToRespawn.e);
                final Player player = Mapper.player.get(playerToRespawn.e);
                controller.resetActions();
                player.invulnerable = true;

                // Portrait state is determined by how many times the player has died.
                final int curPortraitState = player.deaths / Constants.DEATHMATCH_LIVES_AT_EACH_PORTRAIT_STATE;
                player.portraitRender.setNewSpriteImage(
                        player.portraitPortrait.portraitPairs[curPortraitState].blink, 1);

                Timer.schedule(new Timer.Task()
                {
                    @Override
                    public void run()
                    {
                        player.invulnerable = false;
                        player.portraitRender.setNewSpriteImage(
                                player.portraitPortrait.portraitPairs[curPortraitState].normal, 1);
                    }
                }, Constants.RESPAWN_INVULNERABLE_PERIOD_SEC);

                playerToRespawn.addRemovedComponents();
            }
        }, respawnTime);
    }

    public void respawnDeadPlayers()
    {
        for (PlayerToRespawn playerToRespawn : deadPlayers)
        {
            Body runnerBody = PhysicsFactory.createRunnerBody(playerToRespawn.e);

            Mapper.dynamicPhysics.get(playerToRespawn.e).body = runnerBody;
            Mapper.platformer.get(playerToRespawn.e).footContacts.clear();
            final PlayerController controller = Mapper.playerController.get(playerToRespawn.e);
            controller.resetActions();

            final Player player = Mapper.player.get(playerToRespawn.e);
            player.portraitRender.setNewSpriteImage(
                    player.portraitPortrait.portraitPairs[0].normal, 1);
            player.reset();

            // Reset player colors
            playerToRespawn.removedRender.sprites[0].setColor(Color.WHITE);

            playerToRespawn.addRemovedComponents();
        }

        deadPlayers.clear();
    }
    
    private class PlayerToRespawn
    {
        final Entity e;
        final Render removedRender;
        final PlayerAnimation removedPlayerAnimation;
        
        public PlayerToRespawn(Entity e, Render removedRender, PlayerAnimation removedPlayerAnimation)
        {
            this.e = e;
            this.removedRender = removedRender;
            this.removedPlayerAnimation = removedPlayerAnimation;
        }
        
        public void addRemovedComponents()
        {
            e.add(removedRender);
            e.add(removedPlayerAnimation);
        }
    }
}
