package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.AssetLoader;
import com.blakeandshahan.cirque.Constants;

/*
    The Render component is attached to
    entities that have a visual in-game
    representation.
 */

public class Render extends Component implements Pool.Poolable
{
    public Sprite[] sprites;

    public boolean flipped;

    public Layer layer;
    public boolean isVisible;

    public Render init(String[] textureNames, Layer layer, Vector2 position, boolean rotationOriginAtCenter)
    {
        sprites = new Sprite[textureNames.length];
        for (int i = 0; i < sprites.length; i++)
        {
            // TODO: May eventually have issues with needing to retrieve animations.
            final TextureRegion texture = getTexture(textureNames[i]);
            if (texture != null)
                sprites[i] = new Sprite(texture);
            else
                sprites[i] = new Sprite();

            sprites[i].setPosition(position.x, position.y);
        }

        if (!rotationOriginAtCenter)
        {
            for (Sprite sprite : sprites)
            {
                sprite.setOrigin(0, 0);
            }
        }

        this.layer = layer;
        this.flipped = false;

        this.isVisible = true;

        return this;
    }

    public Render init(String textureName, Layer layer, Vector2 position, boolean rotationOriginAtCenter)
    {
        return init(new String[]{textureName}, layer, position, rotationOriginAtCenter);
    }

    public Render init(Layer layer, Vector2 position, boolean rotationOriginAtCenter)
    {
        return init(Constants.SHAHAN_IDLE, layer, position, rotationOriginAtCenter);
    }

    private TextureRegion getTexture(String textureName)
    {
        if (textureName != null)
        {
            TextureRegion textureRegion = AssetLoader.getTextureRegion(textureName);

            if (textureRegion == null)
            {
                textureRegion = AssetLoader.getAnimation(textureName).getKeyFrame(0);
            }
            return textureRegion;
        }

        return null;
    }

    public void draw(SpriteBatch batch)
    {
        for (Sprite sprite : sprites)
        {
            if (sprite.getTexture() != null)
                sprite.draw(batch);
        }
    }

    public void setPosition(Vector2 position)
    {
        for (Sprite sprite : sprites)
            sprite.setPosition(position.x, position.y);
    }

    public void setRotation(float angle)
    {
        for (Sprite sprite : sprites)
            sprite.setRotation(angle);
    }

    public void setNewSpriteImage(String textureName, int layerPosition)
    {
        Constants.setSpriteTexture(sprites[layerPosition], AssetLoader.getTextureRegion(textureName));
    }

    public void flipSprites(boolean xFlip, boolean yFlip)
    {
        for (Sprite sprite : sprites)
            sprite.flip(xFlip, yFlip);
    }

    @Override
    public void reset() {}

    public enum Layer
    {
        BACKGROUND,
        PLAYERS,
        ABILITIES,
        ARENA,
        UI,
        PARTICLES;

        public int getLayerId()
        {
            return ordinal();
        }
    }
}
