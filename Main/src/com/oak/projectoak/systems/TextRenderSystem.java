package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oak.projectoak.components.TextRender;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class TextRenderSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<TextRender> trm;

    private SpriteBatch batch;
    private BitmapFont bitMapFont;

    public TextRenderSystem()
    {
        super(Aspect.getAspectForAll(TextRender.class));

        bitMapFont = new BitmapFont();
    }

    @Override
    protected void initialize()
    {
        batch = new SpriteBatch();
    }

    @Override
    protected boolean checkProcessing()
    {
        return true;
    }

    @Override
    protected void begin()
    {
        batch.begin();
    }

    @Override
    protected void process(Entity e)
    {
        TextRender textRender = trm.get(e);

        batch.setColor(textRender.r, textRender.g, textRender.b, textRender.a);
        bitMapFont.draw(batch, textRender.text, textRender.position.x, textRender.position.y);
    }

    @Override
    protected void end()
    {
        batch.end();
    }
}
