package com.oak.projectoak.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.oak.projectoak.Constants;
import com.oak.projectoak.screens.GameScreen;
import com.oak.projectoak.screens.TitleScreen;

public class CameraZoomTransitionSystem extends VoidEntitySystem
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
    protected void processSystem()
    {
        if (zoomingInwards)
            camera.zoom -= zoomVel;
        else
            camera.zoom += zoomVel;

        if ((!zoomingInwards && camera.zoom >= finalCameraZoom) ||
                (zoomingInwards && camera.zoom < finalCameraZoom))
        {
            if (game == null)
                world.deleteSystem(this);
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
