package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Constants
{
    public static int curPlayersActive = 0;

    public static final int MAX_NUM_OF_PLAYERS = 4;

    public static final Vector2 ARENA_CENTER = new Vector2(
            ConvertPixelsToMeters(Gdx.graphics.getWidth() / 2),
            ConvertPixelsToMeters(Gdx.graphics.getHeight() / 2));

    public class Groups
    {
        public static final String PLAYERS = "players";
    }

    public static final float DEFAULT_FRAME_DURATION = .1f;

    public static final int PLAYER_LAT_ACCEL = 3;
    public static final int PLAYER_LAT_MAX_VEL = 4;
    public static final int PLAYER_JUMP_ACCEL = 16;

    public static final Vector2 GRAVITY = Vector2.Zero;
    public static final int ARENA_RADIUS = 4;
    public static final int ARENA_CIRCLE_VERTEX_COUNT = ARENA_RADIUS * 10;

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

    public static final int NUM_OF_CONTROLS = 3;
    public static final int P1_LEFT_KEY = Input.Keys.A;
    public static final int P1_RIGHT_KEY = Input.Keys.D;
    public static final int P1_JUMP_KEY = Input.Keys.W;
    public static final int P2_LEFT_KEY = Input.Keys.LEFT;
    public static final int P2_RIGHT_KEY = Input.Keys.RIGHT;
    public static final int P2_JUMP_KEY = Input.Keys.UP;
}
