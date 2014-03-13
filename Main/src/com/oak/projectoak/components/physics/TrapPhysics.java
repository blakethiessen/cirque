package com.oak.projectoak.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.oak.projectoak.physics.PhysicsFactory;

public class TrapPhysics extends Component
{
    public Fixture fixture;
    public final float initialRadialPosition;

    public boolean onOutsideEdge;
    public final float initialRotation;
    public float trapHeight;

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

        final Vector2 localPosition = trapRingBody.getLocalPoint(position);

        // Rotate the vertices
        initialRotation = onOutsideEdge ? (float)(initialRadialPosition - Math.PI / 2) : (float)(initialRadialPosition + Math.PI / 2);
        for (int i = 1; i < shapeVertices.length; i++)
        {
            shapeVertices[i].rotateRad(initialRotation);
        }

        // position the shape relative to the body.
        for (Vector2 vertex : shapeVertices)
        {
            vertex.add(localPosition);
        }

        shape.set(shapeVertices);

        fixture = PhysicsFactory.createTrapFixture(trapFixtureDef, trapRingBody, shape);
    }
}
