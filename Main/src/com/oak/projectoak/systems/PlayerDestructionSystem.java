package com.oak.projectoak.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.physics.PhysicsFactory;
import gamemodemanagers.GameModeManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerDestructionSystem extends VoidEntitySystem
{
    @Mapper ComponentMapper<DynamicPhysics> dpm;
    @Mapper ComponentMapper<Platformer> platm;
    @Mapper ComponentMapper<Player> playm;
    @Mapper ComponentMapper<ArenaTransform> am;

    private final GameModeManager deathMatchManager;
    private final int respawnTime;
    private final ArrayList<Entity> entitiesToDestroy;
    private final World b2world;

    public PlayerDestructionSystem(World b2world, GameModeManager gameModeManager, int respawnTime)
    {
        this.deathMatchManager = gameModeManager;
        entitiesToDestroy = new ArrayList<Entity>();
        this.b2world = b2world;
        this.respawnTime = respawnTime;
    }

    public void destroyEntity(Entity entity)
    {
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
            Entity e = entitiesToDestroy.get(0);

            if (dpm.has(e))
                b2world.destroyBody(dpm.get(e).body);

            e.disable();

            if (!deathMatchManager.isGameOver())
                setupPlayerRespawnTimer(e);

            entitiesToDestroy.remove(e);
        }
    }

    private void setupPlayerRespawnTimer(final Entity e)
    {

        Timer timer = new Timer();

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Body runnerBody = PhysicsFactory.createRunnerBody(e);
                ArenaTransform arenaTransform = am.get(e);
                runnerBody.setTransform(Constants.ConvertRadialTo2DPosition(
                        (float)(arenaTransform.radialPosition + Math.PI), arenaTransform.onOutsideEdge), 0);

                dpm.get(e).body = runnerBody;
                platm.get(e).footContactCount = 0;
                playm.get(e).resetActions();

                e.enable();
            }
        }, respawnTime);
    }
}
