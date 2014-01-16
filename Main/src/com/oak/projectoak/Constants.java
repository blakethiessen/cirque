package com.oak.projectoak;

import com.badlogic.gdx.math.Vector2;

public class Constants
{
    public class Groups
    {
        public static final String PLAYERS = "players";
    }

    public static final float DEFAULT_FRAME_DURATION = .1f;

    public static final Vector2 GRAVITY = new Vector2(0, -10);

    public static final float METERS_TO_PIXELS = 100f;
    public static final float PIXELS_TO_METERS = .01f;

    // Note: All measurements should be in Box coordinates
    // except when drawing a texture to the world.
    public static float ConvertMetersToPixels(float box2dCoord)
    {
        return box2dCoord * METERS_TO_PIXELS;
    }
    public static float ConvertPixelsToMeters(float worldCoord)
    {
        return worldCoord * PIXELS_TO_METERS;
    }

    //endregion
}
