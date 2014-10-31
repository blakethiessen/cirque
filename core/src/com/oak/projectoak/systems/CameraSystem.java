package com.oak.projectoak.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

/*
    The CameraSystem updates the camera every game loop.
 */

public class CameraSystem extends VoidEntitySystem
{
    private OrthographicCamera camera;
    private boolean enabled;

    public CameraSystem(OrthographicCamera camera, boolean enabled)
    {
        this.camera = camera;
        this.enabled = enabled;
    }

    @Override
    protected void processSystem()
    {
        camera.update();
    }

    @Override
    protected boolean checkProcessing()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
