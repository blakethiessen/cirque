package com.oak.projectoak.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;

public class TitleScreen implements Screen, InputProcessor
{
    private final Game game;
    OrthographicCamera camera;

    SpriteBatch batch;
    Sprite arenaRing;
    Sprite logo;

    boolean beginZoomTransition = false;
    private float rotationSpeed = .2f;
    private float zoomIncreasePerFrame = .002f;
    private float logoAlpha = 1;

    public TitleScreen(Game game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        batch = new SpriteBatch();

        // Setup asset loading
        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = (float)Constants.CAMERA_ZOOM_TO_RESOLUTION_SCALE / (float)Gdx.graphics.getHeight();

        arenaRing = new Sprite(AssetLoader.getTextureRegion(Constants.OUTER_RING));
        arenaRing.setPosition((Gdx.graphics.getWidth() - arenaRing.getWidth()) / 2, (Gdx.graphics.getHeight() - arenaRing.getHeight()) / 2);

        logo = new Sprite(AssetLoader.getTextureRegion(Constants.TITLE_LOGO));
        logo.setPosition((Gdx.graphics.getWidth() - logo.getWidth()) / 2, (Gdx.graphics.getHeight() - logo.getHeight()) / 2);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (beginZoomTransition)
        {
            arenaRing.rotate(rotationSpeed += .05f);
            camera.zoom -= (zoomIncreasePerFrame += Constants.CAMERA_TRANSITION_ZOOM_ACCEL);

            if ((logoAlpha -= .01f) < 0)
                logoAlpha = 0;

            logo.setColor(1, 1, 1, logoAlpha);

            if (camera.zoom <= 0)
            {
                game.setScreen(new GameScreen(game));
            }
        }
        else
            arenaRing.rotate(rotationSpeed);

        draw();
    }

    private void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        arenaRing.draw(batch);
        logo.draw(batch);

        batch.end();
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void show()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.ENTER)
        {
            beginZoomTransition = true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
