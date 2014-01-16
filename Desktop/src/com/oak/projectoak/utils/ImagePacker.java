package com.oak.projectoak.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

/*
    ImagePacker packs individual images into
    one larger image that exists in assets/textures.
    This makes it more optimal for rendering images.
 */

public class ImagePacker
{
    public static void run()
    {
        Settings settings = new Settings();
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.pot = false;
        TexturePacker2.process(
                settings, "textures-original", "textures", "pack");
    }
}
