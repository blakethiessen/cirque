package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.AssetLoader;

/*
    The Render component is attached to
    entities that have a visual in-game
    representation.
 */

public class Render extends Component
{
    public Vector2 position;

    public TextureRegion currentTexture;

    public float r = 1;
    public float g = 1;
    public float b = 1;
    public float a = 1;
    public float scaleX = 1;
    public float scaleY = 1;
    public float rotation;
    public boolean flipped;

    public Layer layer;
    public boolean isVisible;

    public Render(String textureName, Layer layer, Vector2 position)
    {
        currentTexture = AssetLoader.getTextureRegion(textureName);

        this.position = position;

        this.layer = layer;
        this.flipped = false;

        isVisible = true;
    }

    public Render(String textureName, Layer layer)
    {
        this(textureName, layer, Vector2.Zero);
    }

    public enum Layer
    {
        DEFAULT,
        BACKGROUND,
        ACTORS_1,
        ACTORS_2,
        ACTORS_3,
        PARTICLES;

        public int getLayerId()
        {
            return ordinal();
        }
    }
}
