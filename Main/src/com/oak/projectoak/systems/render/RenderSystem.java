package com.oak.projectoak.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.UI;

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

    private TextureRegion backgroundTexture;
    final int BACKGROUND_X;
    final int BACKGROUND_Y;

    private List<Entity> sortedEntities;

    public RenderSystem(OrthographicCamera camera, String backgroundTextureName)
    {
        super(Aspect.getAspectForAll(Render.class).exclude(UI.class));

        batch = new SpriteBatch();
        this.camera = camera;

        backgroundTexture = AssetLoader.getTextureRegion(backgroundTextureName);
        BACKGROUND_X = (Gdx.graphics.getWidth() - backgroundTexture.getRegionWidth()) / 2;
        BACKGROUND_Y = (Gdx.graphics.getHeight() - backgroundTexture.getRegionHeight()) / 2;
    }

    @Override
    protected void initialize()
    {
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

        batch.disableBlending();
//        batch.draw(backgroundTexture, BACKGROUND_X, BACKGROUND_Y);
        batch.enableBlending();
    }

    protected void process(Entity e)
    {
        Render render = sm.get(e);

        if (render != null && render.isVisible)
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
