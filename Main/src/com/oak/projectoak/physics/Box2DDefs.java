package com.oak.projectoak.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.oak.projectoak.Constants;

/*
    Box2DDefs are the physics definitions that shape
    any physics object you want to create. To create
    a new physics shape, create a create...Shape(),
    a create...FixtureDef(), and a create...Body(), with
    corresponding constants.
 */
public class Box2DDefs
{
    // Foot is just wide enough to cover most of the bottom, but doesn't register
    // as collision when sliding down walls.
    private static final float PLAYER_FOOT_HALF_WIDTH = Constants.PLAYER_WIDTH / 20 * 9;
    private static final float PLAYER_FOOT_HALF_HEIGHT = .02f;

    // Shapes
    private static final PolygonShape PLAYER_TORSO_SHAPE = createPlayerTorsoShape();
    private static final PolygonShape PLAYER_FOOT_SHAPE = createPlayerFootShape();
    private static final EdgeShape FLOOR_SHAPE = createFloorShape();
    private static final ChainShape INNER_CIRCLE_SHAPE = createCircleShape(Constants.ARENA_INNER_RADIUS);
    private static final ChainShape OUTER_CIRCLE_SHAPE = createCircleShape(Constants.ARENA_OUTER_RADIUS);
    private static final PolygonShape STAKE_SHAPE = createStake();

    // FixtureDefs
    public static final FixtureDef PLAYER_TORSO = createPlayerTorso();
    public static final FixtureDef PLAYER_FOOT_SENSOR = createPlayerFootSensor();
    public static final FixtureDef FLOOR_FIXTURE_DEF = createFloorFixtureDef();
    public static final FixtureDef INNER_CIRCLE_FIXTURE_DEF = createInnerCircleFixtureDef();
    public static final FixtureDef OUTER_CIRCLE_FIXTURE_DEF = createOuterCircleFixtureDef();
    public static final FixtureDef STAKE_FIXTURE_DEF = createStakeFixtureDef();

    // BodyDefs
    public static final BodyDef PLAYER_BODY_DEF = createPlayerBodyDef();
    public static final BodyDef FLOOR_BODY_DEF = createFloorBodyDef();
    public static final BodyDef CIRCLE_BODY_DEF = createCircleBodyDef();
    public static final BodyDef STAKE_BODY_DEF = createStakeBodyDef();

    // Shape constructors
    private static PolygonShape createPlayerTorsoShape()
    {
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2, new Vector2(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2), 0);

        return shape;
    }

    private static PolygonShape createPlayerFootShape()
    {
        PolygonShape shape = new PolygonShape();
        // This foot shape may have issues with walking up slanted terrain.
        shape.setAsBox(PLAYER_FOOT_HALF_WIDTH, PLAYER_FOOT_HALF_HEIGHT,
                new Vector2(Constants.PLAYER_WIDTH / 2, -PLAYER_FOOT_HALF_HEIGHT), 0);

        return shape;
    }

    private static EdgeShape createFloorShape()
    {
        EdgeShape shape = new EdgeShape();
        shape.set(0, 2, 20, 2);

        return shape;
    }

    private static ChainShape createCircleShape(float circleRadius)
    {
        Vector2[] edges = new Vector2[Constants.ARENA_CIRCLE_VERTEX_COUNT];

        float angle = (float)(2 * Math.PI / Constants.ARENA_CIRCLE_VERTEX_COUNT);

        for (int i = 0; i < Constants.ARENA_CIRCLE_VERTEX_COUNT; i ++)
        {
            edges[i] = new Vector2((float)(circleRadius * Math.cos(angle * i)),
                    (float)(circleRadius * Math.sin(angle * i)));
        }

        ChainShape shape = new ChainShape();
        shape.createLoop(edges);

        return shape;
    }

    private static PolygonShape createStake()
    {
        PolygonShape shape = new PolygonShape();
        shape.set(new float[]
                {
                    0, 0,
                    Constants.STAKE_WIDTH / 2, Constants.STAKE_HEIGHT,
                    Constants.STAKE_WIDTH, 0
                });

        return shape;
    }

    // Fixture Def constructors
    private static FixtureDef createPlayerTorso()
    {
        FixtureDef torso = new FixtureDef();
        torso.shape = PLAYER_TORSO_SHAPE;
        torso.density = .3f;
        torso.friction = .2f;

        return torso;

    }

    private static FixtureDef createPlayerFootSensor()
    {
        FixtureDef footSensor = new FixtureDef();
        footSensor.shape = PLAYER_FOOT_SHAPE;
        footSensor.isSensor = true;

        return footSensor;
    }

    private static FixtureDef createFloorFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = FLOOR_SHAPE;
        fixtureDef.density = 1;
        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    private static FixtureDef createInnerCircleFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = INNER_CIRCLE_SHAPE;
        fixtureDef.density = 1;
        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    private static FixtureDef createOuterCircleFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = OUTER_CIRCLE_SHAPE;
        fixtureDef.density = 1;
        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    private static FixtureDef createStakeFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = STAKE_SHAPE;
        fixtureDef.density = 1;
        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    // Body Def constructors
    private static BodyDef createPlayerBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        return bodyDef;
    }

    private static BodyDef createFloorBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;

        return bodyDef;
    }

    private static BodyDef createCircleBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;

        return bodyDef;
    }

    private static BodyDef createStakeBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;

        return bodyDef;
    }
}

