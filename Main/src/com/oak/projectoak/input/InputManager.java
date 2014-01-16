package com.oak.projectoak.input;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/*
    The InputManager allows a hierarchy of input systems
    to go through a key press. For example, the top level
    input system will handle general key presses (esc to pause game)
    while the a 2nd input system can in-game action key presses.
 */

public class InputManager implements InputProcessor
{
    private World world;
    private InputMultiplexer inputMultiplexer;

    public InputManager(World world)
    {
        this.world = world;

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void addInputProcessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.addProcessor(inputProcessor);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
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
