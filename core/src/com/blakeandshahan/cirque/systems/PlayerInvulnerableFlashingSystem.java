package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Player;
import com.blakeandshahan.cirque.components.Render;

public class PlayerInvulnerableFlashingSystem extends IteratingSystem
{
    public PlayerInvulnerableFlashingSystem()
    {
        super(Family.getFor(Player.class));
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        Player player = Mapper.player.get(e);

        if (player.invulnerable)
        {
            Render render = Mapper.render.get(e);
            if (render != null)
                render.sprites[0].setColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);

            player.wasInvulnerableLastFrame = true;
        }
        else if (player.wasInvulnerableLastFrame)
        {
            Render render = Mapper.render.get(e);
            render.sprites[0].setColor(1, 1, 1, 1);

            player.wasInvulnerableLastFrame = false;
        }
    }
}
