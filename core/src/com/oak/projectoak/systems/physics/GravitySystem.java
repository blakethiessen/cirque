package com.oak.projectoak.systems.physics;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.oak.projectoak.Constants;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;

public class GravitySystem extends IteratingSystem
{
    private final Vector2 arenaCenter;

    public GravitySystem(Vector2 arenaCenter)
    {
        super(Family.getFor(ArenaTransform.class, DynamicPhysics.class));
        this.arenaCenter = arenaCenter;
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        DynamicPhysics physics = Mapper.dynamicPhysics.get(e);
        ArenaTransform arenaTransform = Mapper.arenaTransform.get(e);

        Body body = physics.body;
        Vector2 position = body.getPosition();

        // Apply radial gravitational force
        Vector2 curGravityVector = arenaCenter.cpy();
        curGravityVector.sub(position);

        if (!arenaTransform.onOutsideEdge)
        {
            // Key difference between internal and external
            // gravity system, gravity needs to be flipped.
            curGravityVector.scl(-1);
        }

        body.applyForceToCenter(curGravityVector, true);

        physics.curGravityVec = curGravityVector;

        // Set the player's feet towards the center of the circle.
        double rotation;
        if (arenaTransform.onOutsideEdge)
            rotation = Math.atan2(curGravityVector.x, -curGravityVector.y) - Constants.ROTATIONAL_OFFSET;
        else
            rotation = Math.atan2(curGravityVector.x, -curGravityVector.y) + Constants.ROTATIONAL_OFFSET;

        body.setTransform(position, (float)rotation);

        if (arenaTransform.onOutsideEdge)
            arenaTransform.radialPosition = (float)(rotation + Math.PI / 2);
        else
            arenaTransform.radialPosition = (float)(rotation - Math.PI / 2);
    }
}
