package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.UI;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class UIRenderSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Render> sm;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public UIRenderSystem(OrthographicCamera camera)
    {
        super(Aspect.getAspectForAll(Render.class, UI.class));
        this.camera = camera;
    }

    @Override
    protected void initialize()
    {
        batch = new SpriteBatch();
    }

    @Override
    protected void begin()
    {
//        batch.setProjectionMatrix(camera.combined);
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

