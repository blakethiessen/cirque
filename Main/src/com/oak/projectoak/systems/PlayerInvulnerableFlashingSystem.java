package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.Render;

public class PlayerInvulnerableFlashingSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<Render> rm;

    public PlayerInvulnerableFlashingSystem()
    {
        super(Aspect.getAspectForAll(Player.class));
    }

    @Override
    protected void process(Entity e)
    {
        Player player = pm.get(e);

        if (player.invulnerable)
        {
            Render render = rm.get(e);
            render.r = (float)Math.random();
            render.g = (float)Math.random();
            render.b = (float)Math.random();

            player.wasInvulnerableLastFrame = true;
        }
        else if (player.wasInvulnerableLastFrame)
        {
            Render render = rm.get(e);
            render.r = 1;
            render.g = 1;
            render.b = 1;

            player.wasInvulnerableLastFrame = false;
        }
    }
}
