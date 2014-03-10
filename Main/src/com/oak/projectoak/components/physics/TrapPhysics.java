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

    public TrapPhysics(float[] shapeVertices, FixturePositioningBody trapRingBody, Vector2 position)
    {
        PolygonShape shape = new PolygonShape();

        Body body = trapRingBody.getBody();
        Vector2 localPoint = body.getLocalPoint(position);
        final Vector2 newPos = localPoint.sub(trapRingBody.getFixturePositioningOffset());

        trapRingBody.addToFixturePositioningOffset(newPos.cpy());

        // position the shape relative to the body.
        for (int i = 0, j = 1; j < shapeVertices.length; i += 2, j += 2)
        {
            shapeVertices[i] += newPos.x;
            shapeVertices[j] += newPos.y;
        }

        shape.set(shapeVertices);

        fixture = PhysicsFactory.createTrapFixture(Box2DDefs.STAKE_FIXTURE_DEF, body, shape);
    }
}
