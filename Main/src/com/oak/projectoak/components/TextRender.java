package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class TextRender extends Component
{
    public Vector2 position;

    public String text;

    public float r;
    public float g;
    public float b;
    public float a;

    public TextRender(String text, Vector2 position, float r, float g, float b, float a)
    {
        this.text = text;
        this.position = position;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public TextRender(String text, Vector2 position)
    {
        this(text, position, 1, 1, 1, 1);
    }
}
