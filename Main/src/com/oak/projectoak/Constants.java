package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Constants
{
    public static final String TITLE_LOGO = "cirqueTitle";

    // Animations
    public static final String SHAHAN_WALK = "characters/shahan/walk/walk";
    public static final String SHAHAN_IDLE = "characters/shahan/idle/idle";

    public static final String PORTRAIT_TEAM_RED = "characterPortraits/TEAMCOLORS/red";
    public static final String PORTRAIT_TEAM_BLUE = "characterPortraits/TEAMCOLORS/blue";
    public static final String GAME_OVER_TEXT = "Press Enter to restart game or Escape to re-choose abilities";

    public static final String PIRATE_IDLE = "characters/pirate/idle/idle";
    public static final String PIRATE_WALK = "characters/pirate/walk/walk";
    public static final String PIRATE_JUMP = "characters/pirate/jump/jump";
    public static final String PIRATE_LAY_TRAP = "characters/pirate/lay_trap/lay_trap";
    public static final String PIRATE_DEATH = "characters/pirate/death/death";
    public static final String PIRATE_PORTRAIT_HEALTHY = "characterPortraits/pirate/1_healthy";
    public static final String PIRATE_PORTRAIT_BRUISED = "characterPortraits/pirate/2_bruised";
    public static final String PIRATE_PORTRAIT_NEAR_DEAD = "characterPortraits/pirate/3_near_dead";
    public static final String PIRATE_PORTRAIT_DEAD = "characterPortraits/pirate/4_dead";

    public static final String NINJA_IDLE = "characters/ninja/idle/idle";
    public static final String NINJA_WALK = "characters/ninja/walk/walk";
    public static final String NINJA_JUMP = "characters/ninja/jump/jump";
    public static final String NINJA_LAY_TRAP = "characters/ninja/lay_trap/lay_trap";
    public static final String NINJA_DEATH = "characters/ninja/death/death";
    public static final String NINJA_PORTRAIT_HEALTHY = "characterPortraits/ninja/1_healthy";
    public static final String NINJA_PORTRAIT_BRUISED = "characterPortraits/ninja/2_bruised";
    public static final String NINJA_PORTRAIT_NEAR_DEAD = "characterPortraits/ninja/3_near_dead";
    public static final String NINJA_PORTRAIT_DEAD = "characterPortraits/ninja/4_dead";
    
    public static final String GANGSTA_IDLE = "characters/gangsta/idle/idle";
    public static final String GANGSTA_WALK = "characters/gangsta/walk/walk";
    public static final String GANGSTA_JUMP = "characters/gangsta/jump/jump";
    public static final String GANGSTA_LAY_TRAP = "characters/gangsta/lay_trap/lay_trap";
    public static final String GANGSTA_DEATH = "characters/gangsta/death/death";
    public static final String GANGSTA_PORTRAIT_HEALTHY = "characterPortraits/gangsta/1_healthy";
    public static final String GANGSTA_PORTRAIT_BRUISED = "characterPortraits/gangsta/2_bruised";
    public static final String GANGSTA_PORTRAIT_NEAR_DEAD = "characterPortraits/gangsta/3_near_dead";
    public static final String GANGSTA_PORTRAIT_DEAD = "characterPortraits/gangsta/4_dead";

    public static final String PHARAOH_IDLE = "characters/pharaoh/idle/idle";
    public static final String PHARAOH_WALK = "characters/pharaoh/walk/walk";
    public static final String PHARAOH_JUMP = "characters/pharaoh/jump/jump";
    public static final String PHARAOH_LAY_TRAP = "characters/pharaoh/lay_trap/lay_trap";
    public static final String PHARAOH_DEATH = "characters/pharaoh/death/death";
    public static final String PHARAOH_PORTRAIT_HEALTHY = "characterPortraits/pharaoh/1_healthy";
    public static final String PHARAOH_PORTRAIT_BRUISED = "characterPortraits/pharaoh/2_bruised";
    public static final String PHARAOH_PORTRAIT_NEAR_DEAD = "characterPortraits/pharaoh/3_near_dead";
    public static final String PHARAOH_PORTRAIT_DEAD = "characterPortraits/pharaoh/4_dead";

    public static final String PILLAR = "abilities/pillar/pillar";
    public static final String SPIKE = "abilities/spike/spike";
    public static final String LIGHTNING_BOLT = "abilities/lightning_bolt/lightning_bolt";

    public static final String OUTER_RING = "arena/outerRing";

    public static final String UI_ENERGY_METER_1_LEVEL = "energyMeter/gray_1_notches";
    public static final String UI_ENERGY_METER_2_LEVEL = "energyMeter/gray_2_notches";
    public static final String UI_ENERGY_METER_3_LEVEL = "energyMeter/gray_3_notches";

    public static final String UI_ENERGY_METER_FILL = "energyMeter/energy";

    public static final String UI_ENERGY_READY = "energyMeter/ready/ability_ready";

    public static final String UI_PILLAR_METER = "energyMeter/abilityIcons/pillar";
    public static final String UI_SPIKE_METER = "energyMeter/abilityIcons/spike";
    public static final String UI_LIGHTNING_METER = "energyMeter/abilityIcons/lightning_bolt";
    public static final String UI_RING_OF_FIRE_METER = "energyMeter/abilityIcons/ring_of_fire";
    public static final String UI_SHADOW_CLONE_METER = "energyMeter/abilityIcons/shadow_clone";

    public static final int ENERGY_METER_WIDTH = 70;
    public static final int PORTRAIT_WIDTH = 120;

    public static final float ROTATIONAL_OFFSET = .04f;
    public static final float TIER1_ENERGY_INCREASE_PER_FRAME_OF_RUNNING = .001f;
    public static final int RESPAWN_TIME_SEC = 3;
    public static final long RESPAWN_INVULNERABLE_PERIOD_SEC = 3;

    public static final int DEATHMATCH_NUM_TEAMS = 2;
    public static final int DEATHMATCH_KILLS_TO_WIN = 2;//8;

    public static final float MAX_ARENA_ROTATION_SPEED = 1f;
    public static final float ROTATIONAL_VELOCITY_INCREASE_PER_KILL = MAX_ARENA_ROTATION_SPEED / (DEATHMATCH_KILLS_TO_WIN * DEATHMATCH_NUM_TEAMS - 1);
    public static final float ABILITY_CREATION_DELAY = .15f;
    public static final float TIER1_ABILITY_ENERGY_COST = .33f;
    public static final float JUMP_TIMEOUT_DELAY = .3f;
    public static final int STAKE_LIFETIME = 10;
    public static final float STARTING_TIER1_ABILITY_ENERGY = .33f;
    public static final int PILLAR_DESTRUCTION_TIME_RESET = 8;
    public static final int PILLAR_LIFETIME = 16;
    public static final int PORTRAIT_ENERGY_METER_PADDING = 12;
    public static final float CAMERA_TRANSITION_ZOOM_SPEED = .014f;
    //TODO: Make these factor each other.
    public static final float LIGHTNING_BOLT_SPEED_SCALE_FACTOR = 4f;
    public static final float LIGHTNING_WRAP_AROUND_SPAWN_DISTANCE = 8f;
    public static final float LIGHTNING_TIME_UNTIL_WRAP_AROUND = .4f;
    public static final float LIGHTNING_BOLT_SPAWN_OFFSET = .1f;
    public static final float LIGHTNING_BOLT_ENERGY_COST = .5f;
    public static final int ZOOM_RING_PADDING = 260;

    // GENERAL CONSTANTS
    public static int curPlayersActive = 0;
    public static final int MAX_NUM_OF_PLAYERS = 4;

    public static final int CAMERA_ZOOM_TO_RESOLUTION_SCALE = 1080;
    public static final float CAMERA_TRANSITION_ZOOM_ACCEL = .0002f;

    public class Groups
    {
        public static final String PLAYERS = "players";
    }

    // ANIMATION CONSTANTS
    public static final float DEFAULT_FRAME_DURATION = 1/30f;

    public static final int UI_PADDING = 20;
    public static final Vector2 P1_UI_POSITION = new Vector2(UI_PADDING, Gdx.graphics.getHeight() - UI_PADDING - PORTRAIT_WIDTH);
    public static final Vector2 P2_UI_POSITION = new Vector2(Gdx.graphics.getWidth() - UI_PADDING, Gdx.graphics.getHeight() - UI_PADDING - PORTRAIT_WIDTH);
    public static final Vector2 P3_UI_POSITION = new Vector2(UI_PADDING, UI_PADDING);
    public static final Vector2 P4_UI_POSITION = new Vector2(Gdx.graphics.getWidth() - UI_PADDING, UI_PADDING);

    // PLAYER CONSTANTS
    public static final float PLAYER_WIDTH = .62f;
    public static final float PLAYER_HEIGHT = 1f;

    public static final int PLAYER_LAT_ACCEL = 6;
    public static final int PLAYER_LAT_MAX_VEL = 4;

    public static final int INNER_PLAYER_JUMP_ACCEL = 20;
    public static final int OUTER_PLAYER_JUMP_ACCEL = 25;

    // ABILITY CONSTANTS
    public static final float STAKE_WIDTH = .64f;
    public static final float STAKE_HEIGHT = .65f;

    public static final float PILLAR_WIDTH = .55f;
    public static final float PILLAR_HEIGHT = .55f;
    public static final float PILLAR_X_OFFSET = 0f;

    public static final float PILLAR_STACK_RANGE = .1f;

    public static final float LIGHTNING_BOLT_WIDTH = .45f;
    public static final float LIGHTNING_BOLT_HEIGHT = 1.4f;

    // ARENA CONSTANTS
    public static final Vector2 GRAVITY = Vector2.Zero;
    public static final float ARENA_INNER_RADIUS = 5;
    public static final float ARENA_EDGE_WIDTH = .2f;
    public static final float ARENA_OUTER_RADIUS = Constants.ARENA_INNER_RADIUS + Constants.ARENA_EDGE_WIDTH;
    public static final int ARENA_CIRCLE_VERTEX_COUNT = (int)ARENA_INNER_RADIUS * 10;

    public static final Vector2 ARENA_CENTER = new Vector2(
            ConvertPixelsToMeters(Gdx.graphics.getWidth() / 2),
            ConvertPixelsToMeters(Gdx.graphics.getHeight() / 2));

    public static Vector2 ConvertRadialTo2DPosition(float radialPosition, boolean onOutsideEdge)
    {
        float DISTANCE = onOutsideEdge ? Constants.ARENA_OUTER_RADIUS : Constants.ARENA_INNER_RADIUS;

        return new Vector2((float)(ARENA_CENTER.x + DISTANCE * Math.cos(radialPosition)),
                           (float)(ARENA_CENTER.y + DISTANCE * Math.sin(radialPosition)));
    }

    public static Vector2 ConvertRadialTo2DPositionWithHeight(float radialPosition, boolean onOutsideEdge, float height)
    {
        float DISTANCE = onOutsideEdge ? Constants.ARENA_OUTER_RADIUS + height : Constants.ARENA_INNER_RADIUS - height;

        return new Vector2((float)(ARENA_CENTER.x + DISTANCE * Math.cos(radialPosition)),
                (float)(ARENA_CENTER.y + DISTANCE * Math.sin(radialPosition)));
    }

    public static void setSpriteTexture(Sprite sprite, TextureRegion textureRegion)
    {
        sprite.setRegion(textureRegion);
        sprite.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

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
    public static final int P1_ABILITY_2_KEY = Input.Keys.E;
    public static final int P1_ABILITY_3_KEY = Input.Keys.Z;

    public static final int P2_LEFT_KEY = Input.Keys.LEFT;
    public static final int P2_RIGHT_KEY = Input.Keys.RIGHT;
    public static final int P2_JUMP_KEY = Input.Keys.UP;
    public static final int P2_ABILITY_1_KEY = Input.Keys.SLASH;
    public static final int P2_ABILITY_2_KEY = Input.Keys.APOSTROPHE;
    public static final int P2_ABILITY_3_KEY = Input.Keys.RIGHT_BRACKET;

    // Currently for debug
    public static final int P3_LEFT_KEY = Input.Keys.F;
    public static final int P3_RIGHT_KEY = Input.Keys.H;
    public static final int P3_JUMP_KEY = Input.Keys.T;
    public static final int P3_ABILITY_1_KEY = Input.Keys.R;
    public static final int P3_ABILITY_2_KEY = Input.Keys.Y;
    public static final int P3_ABILITY_3_KEY = Input.Keys.V;

    public static final int P4_LEFT_KEY = Input.Keys.J;
    public static final int P4_RIGHT_KEY = Input.Keys.L;
    public static final int P4_JUMP_KEY = Input.Keys.I;
    public static final int P4_ABILITY_1_KEY = Input.Keys.U;
    public static final int P4_ABILITY_2_KEY = Input.Keys.O;
    public static final int P4_ABILITY_3_KEY = Input.Keys.N;
}
