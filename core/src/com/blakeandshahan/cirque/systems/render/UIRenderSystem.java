package com.blakeandshahan.cirque.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Render;
import com.blakeandshahan.cirque.components.UI;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class UIRenderSystem extends IteratingSystem
{
    private SpriteBatch batch;

    public UIRenderSystem(SpriteBatch spriteBatch)
    {
        super(Family.all(Render.class, UI.class).get());

        batch = spriteBatch;
    }

    protected void processEntity(Entity e, float deltaTime)
    {
        Render render = Mapper.render.get(e);

        if (render.isVisible)
        {
            if (render.flipped)
            {
                if (!render.sprites[0].isFlipX())
                    render.flipSprites(true, false);
            }
            else
            {
                if (render.sprites[0].isFlipX())
                    render.flipSprites(true, false);
            }

            render.draw(batch);
        }
    }
}

