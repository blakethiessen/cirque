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
    private final boolean restartGame;

    private boolean zoomingInwards;

    public CameraZoomTransitionSystem(OrthographicCamera camera, float finalCameraZoom)
    {
        this.camera = camera;
        this.finalCameraZoom = finalCameraZoom;

        if (finalCameraZoom < camera.zoom)
            zoomingInwards = true;
        else
            zoomingInwards = false;

        restartGame = false;
    }

    public CameraZoomTransitionSystem(OrthographicCamera camera, float finalCameraZoom, Game game, GameScreen gameScreen, boolean restartGame)
    {
        this.camera = camera;
        this.finalCameraZoom = finalCameraZoom;
        this.game = game;
        this.gameScreen = gameScreen;
        this.restartGame = restartGame;

        if (finalCameraZoom < camera.zoom)
            zoomingInwards = true;
        else
            zoomingInwards = false;
    }

    @Override
    protected void processSystem()
    {
        if (zoomingInwards)
            camera.zoom -= Constants.CAMERA_TRANSITION_ZOOM_SPEED;
        else
            camera.zoom += Constants.CAMERA_TRANSITION_ZOOM_SPEED;

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
    }
}
