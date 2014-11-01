package com.oak.projectoak.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.Render;

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
