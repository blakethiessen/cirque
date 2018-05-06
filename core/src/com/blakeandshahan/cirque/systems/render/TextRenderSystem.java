package com.blakeandshahan.cirque.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.TextRender;

/*
    The RenderSystem draws all sprites onto the screen.
 */

public class TextRenderSystem extends IteratingSystem
{
    private SpriteBatch batch;
    private BitmapFont bitMapFont;

    public TextRenderSystem(SpriteBatch spriteBatch)
    {
        super(Family.all(TextRender.class).get());

        batch = spriteBatch;
        bitMapFont = new BitmapFont(Gdx.files.internal("fonts/deFonarts_96.fnt"));
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        TextRender textRender = Mapper.textRender.get(e);

        bitMapFont.setColor(textRender.color);
//        bitMapFont.setScale(textRender.scale);
        bitMapFont.draw(batch, textRender.text, textRender.position.x, textRender.position.y);
    }
}
