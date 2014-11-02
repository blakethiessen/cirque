package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.screens.GameScreen;
import com.blakeandshahan.cirque.screens.TitleScreen;

public class CameraZoomTransitionSystem extends EntitySystem
{
    private final OrthographicCamera camera;
    private final float finalCameraZoom;
    private Game game;
    private GameScreen gameScreen;
    private boolean restartGame;

    private float zoomVel;
    private boolean zoomingInwards;

    public CameraZoomTransitionSystem(OrthographicCamera camera, float finalCameraZoom)
    {
        this.camera = camera;
        this.finalCameraZoom = finalCameraZoom;
        zoomVel = Constants.CAMERA_TRANSITION_ZOOM_ACCEL;

        if (finalCameraZoom < camera.zoom)
            zoomingInwards = true;
        else
            zoomingInwards = false;

        restartGame = false;
    }

    public CameraZoomTransitionSystem(
            OrthographicCamera camera, float finalCameraZoom, Game game, GameScreen gameScreen, boolean restartGame)
    {
        this(camera, finalCameraZoom);

        this.game = game;
        this.gameScreen = gameScreen;
        this.restartGame = restartGame;
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
            if (game == null)
                EntityFactory.engine.removeSystem(this);
            else
            {
                gameScreen.dispose();
                if (restartGame)
                    game.setScreen(new GameScreen(game));
                else
                    game.setScreen(new TitleScreen(game));
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
}
