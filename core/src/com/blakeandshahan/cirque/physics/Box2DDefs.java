package com.blakeandshahan.cirque.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.blakeandshahan.cirque.Constants;

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

    // Shape vertice arrays
    public static final Vector2[] getSpikeVertices()
    {
        return new Vector2[]
            {
                new Vector2(0, 0),
                new Vector2(Constants.STAKE_WIDTH, 0),
                new Vector2(Constants.STAKE_WIDTH / 2, Constants.STAKE_HEIGHT)
            };
    }

    public static final Vector2[] getPillarVertices(int numSegmentsHigh)
    {
        float pillarHeight = Constants.PILLAR_HEIGHT * numSegmentsHigh;

        return new Vector2[]
                {
                        new Vector2(Constants.PILLAR_X_OFFSET, 0),
                        new Vector2(Constants.PILLAR_X_OFFSET + Constants.PILLAR_WIDTH, 0),
                        new Vector2(Constants.PILLAR_X_OFFSET + Constants.PILLAR_WIDTH, pillarHeight),
                        new Vector2(Constants.PILLAR_X_OFFSET, pillarHeight)
                };
    }

    // Shapes
    private static final PolygonShape PLAYER_TORSO_SHAPE = createPlayerTorsoShape();
    private static final PolygonShape PLAYER_FOOT_SHAPE = createPlayerFootShape();
    private static final ChainShape INNER_CIRCLE_SHAPE = createCircleShape(Constants.ARENA_INNER_RADIUS);
    private static final ChainShape OUTER_CIRCLE_SHAPE = createCircleShape(Constants.ARENA_OUTER_RADIUS);
    private static final Shape LIGHTNING_BOLT_SHAPE = createLightningBoltShape();

    // FixtureDefs
    public static final FixtureDef PLAYER_TORSO = createPlayerTorso();
    public static final FixtureDef PLAYER_FOOT_SENSOR = createPlayerFootSensor();
    public static final FixtureDef INNER_CIRCLE_FIXTURE_DEF = createInnerCircleFixtureDef();
    public static final FixtureDef OUTER_CIRCLE_FIXTURE_DEF = createOuterCircleFixtureDef();
    public static final FixtureDef STAKE_FIXTURE_DEF = createStakeFixtureDef();
    public static final FixtureDef PILLAR_FIXTURE_DEF = createPillarFixtureDef();
    public static final FixtureDef LIGHTNING_BOLT_FIXTURE_DEF = createLightningBoltFixtureDef();

    // BodyDefs
    public static final BodyDef PLAYER_BODY_DEF = createPlayerBodyDef();
    public static final BodyDef ARENA_BODY_DEF = createArenaBodyDef();
    public static final BodyDef TRAP_RING_BODY_DEF = createTrapRingBodyDef();
    public static final BodyDef GENERIC_ABILITY_BODY_DEF = createGenericAbilityBodyDef();

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

    private static Shape createLightningBoltShape()
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.LIGHTNING_BOLT_WIDTH / 2, Constants.LIGHTNING_BOLT_HEIGHT / 2, new Vector2(Constants.LIGHTNING_BOLT_WIDTH / 2 + .15f, Constants.LIGHTNING_BOLT_HEIGHT / 2), 0);

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

        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    private static FixtureDef createPillarFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.friction = 1f;

        return fixtureDef;
    }

    private static FixtureDef createLightningBoltFixtureDef()
    {
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = LIGHTNING_BOLT_SHAPE;
        fixtureDef.isSensor = true;

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

    private static BodyDef createArenaBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;

        return bodyDef;
    }

    private static BodyDef createTrapRingBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;

        return bodyDef;
    }

    private static BodyDef createGenericAbilityBodyDef()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;

        return bodyDef;
    }

}

