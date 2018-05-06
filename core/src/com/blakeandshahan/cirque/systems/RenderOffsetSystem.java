package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Render;
import com.blakeandshahan.cirque.components.RenderOffset;

public class RenderOffsetSystem extends IteratingSystem
{
    public RenderOffsetSystem()
    {
        super(Family.all(RenderOffset.class, Render.class).get());
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        RenderOffset renderOffset = Mapper.renderOffset.get(e);
        Render render = Mapper.render.get(e);
        Sprite mainSprite = render.sprites[0];

        render.setPosition(new Vector2(mainSprite.getX() - renderOffset.pxOffset.x, mainSprite.getY() - renderOffset.pxOffset.y));
    }
}
