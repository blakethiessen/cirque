package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class TextRender extends Component
{
    public Vector2 position;

    public String text;

    public Color color;

    public TextRender(String text, Vector2 position, Color color)
    {
        this.text = text;
        this.position = position;
        this.color = color;
    }

    public TextRender(String text, Vector2 position)
    {
        this(text, position, Color.BLACK);
    }
}
