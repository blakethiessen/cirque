package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Constants
{
    public static final String SHAHAN_WALK_ANIMATION = "shahan/walk/walk";
    public static final String SHAHAN_IDLE = "shahan/idle/idle";
    // GENERAL CONSTANTS
    public static int curPlayersActive = 0;
    public static final int MAX_NUM_OF_PLAYERS = 4;

    public static final float INITIAL_CAMERA_ZOOM = 1.5f;

    public class Groups
    {
        public static final String PLAYERS = "players";
    }

    // ANIMATION CONSTANTS
    public static final float DEFAULT_FRAME_DURATION = 1/30f;

    // PLAYER CONSTANTS
    public static final float PLAYER_WIDTH = .5f;
    public static final float PLAYER_HEIGHT = 1f;

    public static final int PLAYER_LAT_ACCEL = 6;
    public static final int PLAYER_LAT_MAX_VEL = 4;
    public static final int PLAYER_JUMP_ACCEL = 30;

    // ARENA CONSTANTS
    public static final Vector2 GRAVITY = Vector2.Zero;
    public static final int ARENA_RADIUS = 4;
    public static final int ARENA_CIRCLE_VERTEX_COUNT = ARENA_RADIUS * 10;

    public static final Vector2 ARENA_CENTER = new Vector2(
            ConvertPixelsToMeters(Gdx.graphics.getWidth() / 2),
            ConvertPixelsToMeters(Gdx.graphics.getHeight() / 2));

    // BOX2D CONSTANTS
    public static final float METERS_TO_PIXELS = 100;
    public static final float PIXELS_TO_METERS = 1 / METERS_TO_PIXELS;

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

    // CONTROL CONSTANTS
    public static final int NUM_OF_CONTROLS = 3;

    public static final int P1_LEFT_KEY = Input.Keys.A;
    public static final int P1_RIGHT_KEY = Input.Keys.D;
    public static final int P1_JUMP_KEY = Input.Keys.W;
    public static final int P1_ABILITY_1_KEY = Input.Keys.Q;

    public static final int P2_LEFT_KEY = Input.Keys.LEFT;
    public static final int P2_RIGHT_KEY = Input.Keys.RIGHT;
    public static final int P2_JUMP_KEY = Input.Keys.UP;

    // Currently for debug
    public static final int P3_LEFT_KEY = Input.Keys.F;
    public static final int P3_RIGHT_KEY = Input.Keys.H;
    public static final int P3_JUMP_KEY = Input.Keys.T;

    public static final int P4_LEFT_KEY = Input.Keys.J;
    public static final int P4_RIGHT_KEY = Input.Keys.L;
    public static final int P4_JUMP_KEY = Input.Keys.I;
}
