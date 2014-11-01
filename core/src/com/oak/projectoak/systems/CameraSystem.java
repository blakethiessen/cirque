package com.oak.projectoak.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

/*
    The CameraSystem updates the camera every game loop.
 */

public class CameraSystem extends EntitySystem
{
    private OrthographicCamera camera;
    private boolean enabled;

    public CameraSystem(OrthographicCamera camera, boolean enabled)
    {
        this.camera = camera;
        this.enabled = enabled;
    }

    @Override
    public void update(float deltaTime)
    {
        camera.update();
    }

    @Override
    public boolean checkProcessing()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
