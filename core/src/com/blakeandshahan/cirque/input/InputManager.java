package com.blakeandshahan.cirque.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/*
    The InputManager allows a hierarchy of input systems
    to go through a key press. For example, the top level
    input system will handle general key presses (esc to pause game)
    while the a 2nd input system can in-game action key presses.
 */

public class InputManager
{
    private InputMultiplexer inputMultiplexer;

    public InputManager()
    {
        inputMultiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void addInputProcessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.addProcessor(inputProcessor);
    }
}
