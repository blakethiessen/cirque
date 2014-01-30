package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.InputProcessor;
import com.oak.projectoak.Action;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
    The InputSystem is a 2nd layer of input reception
    that is for in-game players.
 */

public class InputSystem extends EntityProcessingSystem
        implements InputProcessor
{
    @Mapper ComponentMapper<Controller> cm;
    @Mapper ComponentMapper<Player> pm;

    private HashMap<Action, Control>[] controlStates;

    public InputSystem()
    {
        super(Aspect.getAspectForAll(Controller.class, Player.class));

        controlStates = new HashMap[Constants.MAX_NUM_OF_PLAYERS];

        for (int i = 0; i < Constants.MAX_NUM_OF_PLAYERS; i++)
        {
            controlStates[i] = new HashMap<Action, Control>(Constants.NUM_OF_CONTROLS);
        }

        controlStates[0].put(Action.MOVING_LEFT, new Control(Constants.P1_LEFT_KEY, false));
        controlStates[0].put(Action.MOVING_RIGHT, new Control(Constants.P1_RIGHT_KEY, false));
        controlStates[0].put(Action.JUMPING, new Control(Constants.P1_JUMP_KEY, false));

        controlStates[1].put(Action.MOVING_LEFT, new Control(Constants.P2_LEFT_KEY, false));
        controlStates[1].put(Action.MOVING_RIGHT, new Control(Constants.P2_RIGHT_KEY, false));
        controlStates[1].put(Action.JUMPING, new Control(Constants.P2_JUMP_KEY, false));
    }

    @Override
    protected void process(Entity e)
    {
        Controller controller = cm.get(e);
        final int playerArrNum = pm.get(e).playerNum - 1;

        controller.setAction(Action.MOVING_LEFT,
                controlStates[playerArrNum].get(Action.MOVING_LEFT).state);
        controller.setAction(Action.MOVING_RIGHT,
                controlStates[playerArrNum].get(Action.MOVING_RIGHT).state);
        controller.setAction(Action.JUMPING,
                controlStates[playerArrNum].get(Action.JUMPING).state);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        updateControlState(keycode, true);

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        updateControlState(keycode, false);

        return false;
    }

    private void updateControlState(int keycode, boolean state)
    {
        for (HashMap controlState : controlStates)
        {
            if (updatePlayerControlState(controlState, keycode, state))
                return;
        }
    }

    private boolean updatePlayerControlState(HashMap controlState, int keycode, boolean state)
    {
        final Iterator<Map.Entry<Action, Control>> iterator =
                controlState.entrySet().iterator();

        while (iterator.hasNext())
        {
            final Map.Entry<Action, Control> control = iterator.next();

            if (control.getValue().key == keycode)
            {
                control.getValue().state = state;

                return true;
            }
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

    private class Control
    {
        public int key;
        public boolean state;

        public Control(int key, boolean state)
        {
            this.key = key;
            this.state = state;
        }
    }
}

