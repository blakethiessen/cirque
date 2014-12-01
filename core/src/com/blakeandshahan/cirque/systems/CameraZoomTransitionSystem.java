package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.components.Player;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;

public class CameraZoomTransitionSystem extends EntitySystem
{
    private final OrthographicCamera camera;
    private float finalCameraZoom;
    private final GameModeManager gmManager;

    private TransitionType transitionType;

    private float zoomVel;
    private boolean zoomingInwards;

    public CameraZoomTransitionSystem(TransitionType transitionType,
                                      OrthographicCamera camera, float finalCameraZoom, GameModeManager gmManager)
    {
        this.camera = camera;
        this.finalCameraZoom = finalCameraZoom;
        this.gmManager = gmManager;
        zoomVel = Constants.CAMERA_TRANSITION_ZOOM_ACCEL;

        if (finalCameraZoom < camera.zoom)
            zoomingInwards = true;
        else
            zoomingInwards = false;

        this.transitionType = transitionType;
    }

    @Override
    public void update(float deltaTime)
    {
        if (zoomingInwards)
            camera.zoom -= zoomVel;
        else
            camera.zoom += zoomVel;

        if ((!zoomingInwards && camera.zoom >= finalCameraZoom) ||
                (zoomingInwards && camera.zoom < finalCameraZoom))
        {
            switch (transitionType)
            {
                case INITIAL_LOAD:
                    EntityFactory.engine.removeSystem(this);
                    break;
                case RESTART:
                    if (zoomingInwards)
                    {
                        ImmutableArray playerEntities = EntityFactory.engine.getEntitiesFor(Family.getFor(Player.class));
                        for (int i = 0; i < playerEntities.size(); i++)
                        {
                            EntityFactory.addAbilities((Entity)playerEntities.get(i), Constants.DEFAULT_ABILITIES);
                        }
                        gmManager.resetGame();

                        zoomVel = Constants.CAMERA_TRANSITION_ZOOM_ACCEL;
                        zoomingInwards = false;
                        finalCameraZoom = Constants.MIN_CAMERA_ZOOM;
                    }
                    else
                    {
                        EntityFactory.engine.removeSystem(this);
                    }
                    break;
            }
        }
        else if ((!zoomingInwards && camera.zoom >= finalCameraZoom / 2 + .01f) ||
                (zoomingInwards && camera.zoom < finalCameraZoom / 2 - .01f))
        {
            zoomVel -= Constants.CAMERA_TRANSITION_ZOOM_ACCEL;
        }
        else
            zoomVel += Constants.CAMERA_TRANSITION_ZOOM_ACCEL;
    }

    public enum TransitionType
    {
        INITIAL_LOAD,
        RESTART
    }
}
