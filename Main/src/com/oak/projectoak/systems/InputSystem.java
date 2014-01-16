package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.oak.projectoak.Action;
import com.oak.projectoak.components.*;

/*
    The InputSystem is a 2nd layer of input reception
    that is for in-game players.
 */

public class InputSystem extends EntityProcessingSystem
        implements InputProcessor
{
    @Mapper ComponentMapper<Controller> cm;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean jumping = false;

    public InputSystem()
    {
        super(Aspect.getAspectForAll(Controller.class, Player.class));
    }

    @Override
    protected void process(Entity e)
    {
        Controller controller = cm.get(e);

        controller.setAction(Action.MOVING_LEFT, movingLeft);
        controller.setAction(Action.MOVING_RIGHT, movingRight);
        controller.setAction(Action.JUMPING, jumping);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
        {
            movingLeft = true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
        {
            movingRight = true;
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            jumping = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
        {
            movingLeft = false;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
        {
            movingRight = false;
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            jumping = false;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}

