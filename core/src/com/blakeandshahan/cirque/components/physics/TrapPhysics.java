package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.physics.PhysicsFactory;

public class TrapPhysics extends Component
{
    public Fixture fixture;
    public final float initialRadialPosition;

    public boolean onOutsideEdge;
    public final float initialRotation;
    public float trapHeight;

    public final Vector2 localPosition;

    public TrapPhysics(FixtureDef trapFixtureDef, Vector2[] shapeVertices, Body trapRingBody,
                       Vector2 position, float radialPosition, boolean onOutsideEdge, float trapHeight)
    {
        this(trapFixtureDef, shapeVertices, trapRingBody, position, radialPosition, onOutsideEdge);
        this.trapHeight = trapHeight;
    }

    public TrapPhysics(FixtureDef trapFixtureDef, Vector2[] shapeVertices, Body trapRingBody,
                       Vector2 position, float radialPosition, boolean onOutsideEdge)
    {
        this.onOutsideEdge = onOutsideEdge;
        this.initialRadialPosition = radialPosition - trapRingBody.getAngle();
        this.trapHeight = 0;

        PolygonShape shape = new PolygonShape();

        this.localPosition = trapRingBody.getLocalPoint(position).cpy();
        initialRotation = onOutsideEdge ?
                (float)(initialRadialPosition - Math.PI / 2) : (float)(initialRadialPosition + Math.PI / 2);

        Constants.adjustFixtureTransform(shapeVertices, localPosition, initialRotation);

        shape.set(shapeVertices);

        fixture = PhysicsFactory.createTrapFixture(trapFixtureDef, trapRingBody, shape);
    }
}
