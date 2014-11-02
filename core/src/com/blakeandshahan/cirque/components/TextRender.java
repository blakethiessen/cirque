package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class TextRender extends Component
{
    public Vector2 position;
    public String text;
    public float scale;
    public Color color;
    public float alignmentSize;
    public BitmapFont.HAlignment alignment;

    public TextRender(String text, Vector2 position, Color color, float scale, float alignmentSize, BitmapFont.HAlignment alignment)
    {
        this.text = text;
        this.position = position;
        this.color = color;
        this.scale = scale;
        this.alignmentSize = alignmentSize;
        this.alignment = alignment;
    }


    public TextRender(String text, Vector2 position, Color color, float scale)
    {
        this.text = text;
        this.position = position;
        this.color = color;
        this.scale = scale;
        this.alignmentSize = 0;
        this.alignment = BitmapFont.HAlignment.CENTER;
    }

    public TextRender(String text, Vector2 position)
    {
        this(text, position, Color.BLACK, 1, 0, BitmapFont.HAlignment.CENTER);
    }



}
