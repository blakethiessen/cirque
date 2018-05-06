package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class TextRender extends Component implements Pool.Poolable
{
    public Vector2 position;
    public String text;
    public float scale;
    public Color color;
    public float alignmentSize;
//    public BitmapFont.Align alignment;

    public TextRender init(String text, Vector2 position, Color color,
                      float scale, float alignmentSize)
    {
        this.text = text;
        this.position = position;
        this.color = color;
        this.scale = scale;
        this.alignmentSize = alignmentSize;

        return this;
    }

    public TextRender init(String text, Vector2 position, Color color, float scale)
    {
        return init(text, position, color, scale, 0);
    }

    public TextRender init(String text, Vector2 position)
    {
        return init(text, position, Color.BLACK, 1, 0);
    }


    @Override
    public void reset() {}
}
