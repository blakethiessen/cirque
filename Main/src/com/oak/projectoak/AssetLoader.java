package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/*
    Loads assets (textures, sounds) into memory.
 */

public class AssetLoader
{
    private static HashMap<String, TextureRegion> textureMap;
    private static HashMap<String, Animation> animationMap;

    public static void initialize()
    {
        TextureAtlas[] textureAtlases = new TextureAtlas[2];

        textureAtlases[0] = new TextureAtlas(Gdx.files.internal("textures/pack1/pack1.atlas"),
                Gdx.files.internal("textures/pack1"));
        textureAtlases[1] = new TextureAtlas(Gdx.files.internal("textures/pack2/pack2.atlas"),
                Gdx.files.internal("textures/pack2"));

        loadAtlasIntoMaps(textureAtlases);

//        textureMap.put("background",
//                new TextureRegion(new Texture("textures/background.png")));
    }

    private static void loadAtlasIntoMaps(TextureAtlas[] atlases)
    {
        Array<AtlasRegion> tempTextureList = new Array<AtlasRegion>();
        Array<Array<AtlasRegion>> tempAnimationList = new Array<Array<AtlasRegion>>();

        for (TextureAtlas atlas : atlases)
        {
            for (AtlasRegion atlasRegion : atlas.getRegions())
            {
                // If this texture is not an animation:
                if (atlasRegion.index == -1)
                    tempTextureList.add(atlasRegion);
                else
                {
                    boolean animationAlreadyInList = false;

                    for (Array<AtlasRegion> animationSet : tempAnimationList)
                    {
                        if (animationSet.get(0).name.equals(atlasRegion.name))
                        {
                            animationSet.add(atlasRegion);
                            animationAlreadyInList = true;
                            break; // Break out of loop.
                        }
                    }

                    if (!animationAlreadyInList)
                    {
                        Array<AtlasRegion> newAnimation = new Array<AtlasRegion>();
                        newAnimation.add(atlasRegion);

                        tempAnimationList.add(newAnimation);
                    }
                }
            }
        }

        // Setup maps TODO: If we set the size here, and then add backgrounds, doesn't work?
        textureMap = new HashMap<String, TextureRegion>(tempTextureList.size);
        for (AtlasRegion textureRegion : tempTextureList)
        {
            textureMap.put(textureRegion.name, textureRegion);
        }

        animationMap = new HashMap<String, Animation>(tempAnimationList.size);
        for (Array<AtlasRegion> animationSet : tempAnimationList)
        {
            animationMap.put(animationSet.first().name, new Animation(Constants.DEFAULT_FRAME_DURATION, animationSet));
        }
    }

    public static TextureRegion getTextureRegion(String textureRegion)
    {
        try
        {
            return textureMap.get(textureRegion);
        }
        catch (NullPointerException e)
        {
            Gdx.app.error("Texture not found", "Texture: " + textureRegion + " was not in the textureMap.");
            e.printStackTrace();
        }

        return null;
    }

    public static Animation getAnimation(String animationName)
    {
        try
        {
            return animationMap.get(animationName);
        }
        catch (NullPointerException e)
        {
            Gdx.app.error("Animation not found", "Animation: " + animationName + " was not in the animationMap.");
            e.printStackTrace();
        }

        return null;
    }
}
