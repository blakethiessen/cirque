package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.oak.projectoak.AssetLoader;

/*
    The Render component is attached to
    entities that have a visual in-game
    representation.
 */

public class Render extends Component
{
    public float x;
    public float y;

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

    public Render(String textureName, Layer layer, float x, float y)
    {
        currentTexture = AssetLoader.getTextureRegion(textureName);

        this.x = x;
        this.y = y;
        this.layer = layer;
        this.flipped = false;

        isVisible = true;
    }

    public Render(String textureName, Layer layer)
    {
        this(textureName, layer, 0, 0);
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
