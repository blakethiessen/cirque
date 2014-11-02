package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Animate;
import com.blakeandshahan.cirque.components.Player;
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

    public PlayerDestructionSystem(World b2world, GameModeManager gameModeManager, int respawnTime, ArenaRotationSystem arenaRotationSystem)
    {
        this.deathMatchManager = gameModeManager;
        this.arenaRotationSystem = arenaRotationSystem;
        entitiesToDestroy = new ArrayList<Entity>();
        this.b2world = b2world;
        this.respawnTime = respawnTime;
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

                    // TODO: Find a way to e.disable();

                    if (!deathMatchManager.isGameOver())
                        setupPlayerRespawnTimer(e);

                    arenaRotationSystem.increaseArenaRotationalVelocity();
                }
            }, .6f);

            entitiesToDestroy.remove(e);
        }
    }

    private void setupPlayerRespawnTimer(final Entity e)
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                Body runnerBody = PhysicsFactory.createRunnerBody(e);
                ArenaTransform arenaTransform = Mapper.arenaTransform.get(e);
                runnerBody.setTransform(Constants.ConvertRadialTo2DPosition(
                        (float) (arenaTransform.radialPosition + Math.PI), arenaTransform.onOutsideEdge), 0);

                Mapper.dynamicPhysics.get(e).body = runnerBody;
                Mapper.platformer.get(e).footContacts.clear();
                final Player player = Mapper.player.get(e);
                player.resetActions();
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

                // TODO: Find a way to e.enable();
            }
        }, respawnTime);
    }
}
