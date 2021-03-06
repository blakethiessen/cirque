package com.blakeandshahan.cirque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
    private static HashMap<String, Sound> soundMap;

    private static Music music;

    private static boolean initialized = false;

    public static void initialize()
    {
        if (!initialized)
        {
            TextureAtlas[] textureAtlases = new TextureAtlas[2];

            textureAtlases[0] = new TextureAtlas(Gdx.files.internal("textures/pack1/pack1.atlas"),
                    Gdx.files.internal("textures/pack1"));
            textureAtlases[1] = new TextureAtlas(Gdx.files.internal("textures/pack2/pack2.atlas"),
                    Gdx.files.internal("textures/pack2"));

            loadAtlasIntoMaps(textureAtlases);

        textureMap.put("background",
                new TextureRegion(new Texture("textures/arenaBackground.png")));

            soundMap = new HashMap<String, Sound>();
            soundMap.put("death", Gdx.audio.newSound(Gdx.files.internal("sounds/death_sound_2.wav")));
            soundMap.put("pillar", Gdx.audio.newSound(Gdx.files.internal("sounds/hammer.wav")));
            soundMap.put("jump", Gdx.audio.newSound(Gdx.files.internal("sounds/woosh.wav")));
            soundMap.put("spike", Gdx.audio.newSound(Gdx.files.internal("sounds/spike.wav")));
            soundMap.put("lightning", Gdx.audio.newSound(Gdx.files.internal("sounds/thunder.wav")));
            soundMap.put("ability_ready", Gdx.audio.newSound(Gdx.files.internal("sounds/ability_ready.mp3")));

            initialized = true;
        }
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

    public static TextureRegion getTextureRegion(String textureRegionName)
    {
        final TextureRegion textureRegion = textureMap.get(textureRegionName);

//        if (textureRegion == null)
//            Gdx.app.error("Texture not found", "Texture: " + textureRegionName + " was not in the textureMap.");

        return textureRegion;
    }

    public static Animation getAnimation(String animationName)
    {
        final Animation animation = animationMap.get(animationName);
        if (animation == null)
            Gdx.app.error("Animation not found", "Animation: " + animationName + " was not in the animationMap.");

        return animation;
    }

    public static void playSound(String soundName)
    {
        soundMap.get(soundName).play();
    }

    public static void playMusic()
    {
        music.setVolume(1);
        music.play();
    }

    public static void fadeMusic()
    {
        if (music.getVolume() - Constants.MUSIC_FADE_RATE <= 0)
            music.stop();
        else
        {
            music.setVolume(music.getVolume() - Constants.MUSIC_FADE_RATE);
            fadeMusic();
        }
    }

    public static void setMusic(String musicName)
    {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/" + musicName));
    }
}
