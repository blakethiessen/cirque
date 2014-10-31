package com.oak.projectoak.systems.physics;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.oak.projectoak.Constants;

/*
    The PhysicsDebugSystem renders Box2D wireframes
    (the green and red lines).
 */

public class PhysicsDebugSystem extends VoidEntitySystem
{
    private Box2DDebugRenderer debugRenderer;

    private World b2world;
    private Camera camera;

    public PhysicsDebugSystem(World b2world, Camera camera)
    {
        debugRenderer = new Box2DDebugRenderer();

        this.b2world = b2world;
        this.camera = camera;
    }

    @Override
    protected void processSystem()
    {
        debugRenderer.render(b2world,
                camera.combined.cpy().scale(Constants.METERS_TO_PIXELS, Constants.METERS_TO_PIXELS, 1f));
    }
}
