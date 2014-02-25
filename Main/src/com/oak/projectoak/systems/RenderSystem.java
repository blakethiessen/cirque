package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oak.projectoak.components.Render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class RenderSystem extends EntitySystem
{
    @Mapper ComponentMapper<Render> sm;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private List<Entity> sortedEntities;

    public RenderSystem(OrthographicCamera camera)
    {
        super(Aspect.getAspectForAll(Render.class));
        this.camera = camera;
    }

    @Override
    protected void initialize()
    {
        batch = new SpriteBatch();

        sortedEntities = new ArrayList<Entity>();
    }

    @Override
    protected boolean checkProcessing()
    {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities)
    {
        // Ignore passed in bag, use sorted entities to achieve layering.
        for (Entity e : sortedEntities)
        {
            process(e);
        }
    }

    @Override
    protected void begin()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    protected void process(Entity e)
    {
        Render render = sm.get(e);

        if (render.isVisible)
        {
            if (render.flipped)
            {
                if (!render.sprite.isFlipX())
                    render.sprite.flip(true, false);
            }
            else
            {
                if (render.sprite.isFlipX())
                    render.sprite.flip(true, false);
            }

            render.sprite.draw(batch);
//            batch.draw(render.sprite, render.sprite.getX(), render.sprite.getY());
        }
    }

    @Override
    protected void end()
    {
        batch.end();
    }

    @Override
    protected void inserted(Entity e)
    {
        sortedEntities.add(e);
        Collections.sort(sortedEntities, new Comparator<Entity>()
        {
            @Override
            public int compare(Entity e1, Entity e2)
            {
                Render s1 = sm.get(e1);
                Render s2 = sm.get(e2);
                return s1.layer.compareTo(s2.layer);
            }
        });
    }

    @Override
    protected void removed(Entity e)
    {
        sortedEntities.remove(e);
    }
}
