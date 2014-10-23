package com.oak.projectoak.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.PlayerAnimation;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.gamemodemanagers.GameModeManager;
import com.oak.projectoak.physics.PhysicsFactory;
import com.oak.projectoak.systems.physics.ArenaRotationSystem;

import java.util.ArrayList;

public class PlayerDestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Platformer> platm;
    @Mapper ComponentMapper<Player> playm;
    @Mapper ComponentMapper<ArenaTransform> am;
    @Mapper ComponentMapper<Animate> anm;
    @Mapper ComponentMapper<PlayerAnimation> pam;

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
        playm.get(entity).deaths++;

        if (!entitiesToDestroy.contains(entity))
            entitiesToDestroy.add(entity);
    }

    @Override
    protected boolean checkProcessing()
    {
        return !entitiesToDestroy.isEmpty();
    }

    @Override
    protected void processSystem()
    {
        while (!entitiesToDestroy.isEmpty())
        {
            final Entity e = entitiesToDestroy.get(0);

            Animate animate = anm.get(e);
            animate.setAnimation(pam.get(e).death, true);
            final Player player = playm.get(e);
            player.invulnerable = true;

            Timer.schedule(new Timer.Task()
            {
                @Override
                public void run()
                {
                    if (dpm.has(e))
                        b2world.destroyBody(dpm.get(e).body);

                    e.disable();

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
                ArenaTransform arenaTransform = am.get(e);
                runnerBody.setTransform(Constants.ConvertRadialTo2DPosition(
                        (float) (arenaTransform.radialPosition + Math.PI), arenaTransform.onOutsideEdge), 0);

                dpm.get(e).body = runnerBody;
                platm.get(e).footContacts.clear();
                final Player player = playm.get(e);
                player.resetActions();
                player.invulnerable = true;

                Timer.schedule(new Timer.Task()
                {
                    @Override
                    public void run()
                    {
                        player.invulnerable = false;
                    }
                }, Constants.RESPAWN_INVULNERABLE_PERIOD_SEC);

                e.enable();
            }
        }, respawnTime);
    }
}
