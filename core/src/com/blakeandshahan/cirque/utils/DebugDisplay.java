package com.blakeandshahan.cirque.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Rough and inefficient implementation of a debug display.
// To use: call DebugDisplay.addLine(*your text here*);
public class DebugDisplay
{
    private static BitmapFont font = createWhiteDefaultFont();
    private static CharSequence charSequence = "";

    private static BitmapFont createWhiteDefaultFont()
    {
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);

        return font;
    }

    public static void addLine(String text)
    {
        charSequence = charSequence + "\n" + text;
    }

    public static void draw(SpriteBatch spriteBatch, OrthographicCamera camera)
    {
        // TODO: Draw this on the UI layer when we build that.
        font.drawMultiLine(spriteBatch, charSequence,
                camera.position.x - camera.viewportWidth / 2 + 100,
                camera.position.y + camera.viewportHeight / 2 - 100);

        charSequence = "";
    }
}
