package com.oak.projectoak.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
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

    public TextRenderSystem(SpriteBatch spriteBatch)
    {
        super(Aspect.getAspectForAll(TextRender.class));

        batch = spriteBatch;
        bitMapFont = new BitmapFont(Gdx.files.internal("fonts/deFonarts_96.fnt"));
    }

    @Override
    protected void process(Entity e)
    {
        TextRender textRender = trm.get(e);

        bitMapFont.setColor(textRender.color);
        bitMapFont.setScale(textRender.scale);
        bitMapFont.drawMultiLine(batch, textRender.text, textRender.position.x, textRender.position.y, textRender.alignmentSize, textRender.alignment);

    }
}
