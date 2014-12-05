package com.blakeandshahan.cirque.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.physics.PhysicsFactory;

public class TrapPhysics extends Component implements Pool.Poolable
{
    public Fixture fixture;
    public float initialRadialPosition;

    public boolean onOutsideEdge;
    public float initialRotation;
    public float trapHeight;

    public Vector2 localPosition;

    public TrapPhysics init(FixtureDef trapFixtureDef, Vector2[] shapeVertices, Body trapRingBody,
                       Vector2 position, float radialPosition, boolean onOutsideEdge, float trapHeight)
    {
        this.onOutsideEdge = onOutsideEdge;
        this.initialRadialPosition = radialPosition - trapRingBody.getAngle();
        this.trapHeight = trapHeight;

        PolygonShape shape = new PolygonShape();

        this.localPosition = trapRingBody.getLocalPoint(position).cpy();
        initialRotation = onOutsideEdge ?
                (float)(initialRadialPosition - Math.PI / 2) : (float)(initialRadialPosition + Math.PI / 2);

        Constants.adjustFixtureTransform(shapeVertices, localPosition, initialRotation);

        shape.set(shapeVertices);

        fixture = PhysicsFactory.createTrapFixture(trapFixtureDef, trapRingBody, shape);

        return this;
    }

    public TrapPhysics init(FixtureDef trapFixtureDef, Vector2[] shapeVertices, Body trapRingBody,
                       Vector2 position, float radialPosition, boolean onOutsideEdge)
    {
        return init(trapFixtureDef, shapeVertices, trapRingBody, position, radialPosition, onOutsideEdge, 0);
    }

    @Override
    public void reset() {}
}
