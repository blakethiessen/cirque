package com.oak.projectoak.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.oak.projectoak.physics.Box2DDefs;
import com.oak.projectoak.physics.FixturePositioningBody;
import com.oak.projectoak.physics.PhysicsFactory;

public class TrapPhysics extends Component
{
    public Fixture fixture;
    public float startingRadialPosition;

    public boolean onOutsideEdge;
    public final float initialRotation;

    public TrapPhysics(Vector2[] shapeVertices, FixturePositioningBody trapRingBody, Vector2 position, float radialPosition, boolean onOutsideEdge)
    {
        this.onOutsideEdge = onOutsideEdge;
        this.startingRadialPosition = radialPosition;

        PolygonShape shape = new PolygonShape();

        Body body = trapRingBody.getBody();
        final Vector2 newPos = body.getLocalPoint(position);

        radialPosition = onOutsideEdge ? (float)(radialPosition - Math.PI / 2) : (float)(radialPosition + Math.PI / 2);
        this.initialRotation = radialPosition;

        // Rotate the vertices
        for (int i = 1; i < shapeVertices.length; i++)
        {
            shapeVertices[i].rotateRad(radialPosition);
        }

        // position the shape relative to the body.
        for (Vector2 vertex : shapeVertices)
        {
            vertex.add(newPos);
        }

        shape.set(shapeVertices);

        fixture = PhysicsFactory.createTrapFixture(Box2DDefs.STAKE_FIXTURE_DEF, body, shape);
    }
}
