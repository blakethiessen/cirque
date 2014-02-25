package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.AssetLoader;

/*
    The Render component is attached to
    entities that have a visual in-game
    representation.
 */

public class Render extends Component
{
    public Sprite sprite;

    public boolean flipped;

    public Layer layer;
    public boolean isVisible;

    public Render(String textureName, Layer layer, Vector2 position)
    {
        sprite = new Sprite(AssetLoader.getTextureRegion(textureName));
        sprite.setPosition(position.x, position.y);

        this.layer = layer;
        this.flipped = false;

        isVisible = true;
    }

    public Render(Layer layer, Vector2 position)
    {
        sprite = new Sprite();
        sprite.setPosition(position.x, position.y);

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
