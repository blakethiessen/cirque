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
            render.sprite.setColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);

            player.wasInvulnerableLastFrame = true;
        }
        else if (player.wasInvulnerableLastFrame)
        {
            Render render = rm.get(e);
            render.sprite.setColor(1, 1, 1, 1);

            player.wasInvulnerableLastFrame = false;
        }
    }
}
