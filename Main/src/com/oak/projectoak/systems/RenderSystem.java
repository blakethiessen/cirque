package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.utils.DebugDisplay;


import java.util.*;

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
            batch.setColor(render.r, render.g, render.b, render.a);

            // If the sprite is flipped...
            float flipModifiedX;
            if (render.scaleX < 0)
                flipModifiedX = render.x + render.currentTexture.getRegionWidth();
            else
                flipModifiedX = render.x;

            batch.draw(render.currentTexture, flipModifiedX, render.y, 0, 0,
                    render.currentTexture.getRegionWidth(),
                    render.currentTexture.getRegionHeight(),
                    render.scaleX, render.scaleY, render.rotation);
        }
    }

    @Override
    protected void end()
    {
        DebugDisplay.draw(batch, camera);
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
