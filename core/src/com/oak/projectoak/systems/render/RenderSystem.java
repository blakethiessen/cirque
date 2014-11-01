package com.oak.projectoak.systems.render;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.UI;

import java.util.Comparator;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class RenderSystem extends IteratingSystem
{
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private TextureRegion backgroundTexture;
    final int BACKGROUND_X;
    final int BACKGROUND_Y;

    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;

    public RenderSystem(OrthographicCamera camera, String backgroundTextureName)
    {
        super(Family.getFor(ComponentType.getBitsFor(Render.class), new Bits(0), ComponentType.getBitsFor(UI.class)));

        batch = new SpriteBatch();
        this.camera = camera;

        backgroundTexture = AssetLoader.getTextureRegion(backgroundTextureName);
        BACKGROUND_X = (Gdx.graphics.getWidth() - backgroundTexture.getRegionWidth()) / 2;
        BACKGROUND_Y = (Gdx.graphics.getHeight() - backgroundTexture.getRegionHeight()) / 2;

        renderQueue = new Array<Entity>();

        comparator = new Comparator<Entity>()
        {
            @Override
            public int compare(Entity entityA, Entity entityB)
            {
                return (int)Math.signum(Mapper.render.get(entityA).layer.getLayerId() -
                        Mapper.render.get(entityB).layer.getLayerId());
            }
        };
    }

    @Override
    public void update(float deltaTime)
    {
        // Still do the normal update
        // TODO: Can we do this in a way that doesn't resort every frame?
        super.update(deltaTime);

        renderQueue.sort(comparator);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.disableBlending();
        batch.draw(backgroundTexture, BACKGROUND_X, BACKGROUND_Y);
        batch.enableBlending();

        // Ignore passed in bag, use sorted entities to achieve layering.
        for (int i = renderQueue.size - 1; i >= 0; i--)
        {
            renderSprite(renderQueue.get(i));
        }

        batch.end();

        renderQueue.clear();

    }

    private void renderSprite(Entity e)
    {
        Render render = Mapper.render.get(e);

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

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        renderQueue.add(entity);
    }
}
