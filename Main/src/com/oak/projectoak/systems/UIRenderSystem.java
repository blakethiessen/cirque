package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.UI;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class UIRenderSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Render> sm;

    private SpriteBatch batch;

    public UIRenderSystem()
    {
        super(Aspect.getAspectForAll(Render.class, UI.class));
    }

    @Override
    protected void initialize()
    {
        batch = new SpriteBatch();
    }

    @Override
    protected void begin()
    {
        batch.begin();
    }

    protected void process(Entity e)
    {
        Render render = sm.get(e);

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

    @Override
    protected void end()
    {
        batch.end();
    }
}

