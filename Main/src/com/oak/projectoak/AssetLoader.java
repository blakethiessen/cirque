package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.HashMap;

/*
    Loads assets (textures, sounds) into memory.
 */

public class AssetLoader
{
    private static TextureAtlas atlas;

    private static HashMap<String, TextureRegion> textureMap;
    private static HashMap<String, Animation> animationMap;

    public static void initialize()
    {
        loadAtlasIntoMaps();

//        textureMap.put("background",
//                new TextureRegion(new Texture("textures/background.png")));
    }

    private static void loadAtlasIntoMaps()
    {
        atlas = new TextureAtlas(
                Gdx.files.internal("textures/pack.atlas"),
                Gdx.files.internal("textures"));

        Array<AtlasRegion> tempTextureList = new Array<AtlasRegion>();
        Array<Array<AtlasRegion>> tempAnimationList = new Array<Array<AtlasRegion>>();

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

        // Add extras
        textureMap.put("circle", new TextureRegion(new Texture("textures/circle.png")));
    }

    public static TextureRegion getTextureRegion(String textureRegion)
    {
        // TODO: Throw texture not found exception.
        return textureMap.get(textureRegion);
    }

    public static Animation getAnimation(String animationName)
    {
        // TODO: Throw animation not found exception.
        return animationMap.get(animationName);
    }
}
