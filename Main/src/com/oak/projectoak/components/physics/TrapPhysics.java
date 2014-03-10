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
    public final float initialRadialPosition;

    public boolean onOutsideEdge;
    public final float initialRotation;

    public TrapPhysics(Vector2[] shapeVertices, FixturePositioningBody trapRingBody, Vector2 position, float radialPosition, boolean onOutsideEdge)
    {
        System.out.println(radialPosition);
        this.onOutsideEdge = onOutsideEdge;
        this.initialRadialPosition = radialPosition - trapRingBody.getBody().getAngle();

        PolygonShape shape = new PolygonShape();

        Body body = trapRingBody.getBody();
        final Vector2 localPosition = body.getLocalPoint(position);

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

        fixture = PhysicsFactory.createTrapFixture(Box2DDefs.STAKE_FIXTURE_DEF, body, shape);
    }
}
