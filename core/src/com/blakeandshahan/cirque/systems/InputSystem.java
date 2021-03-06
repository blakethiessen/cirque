package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.blakeandshahan.cirque.Action;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.PlayerController;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;

import java.util.HashMap;

/*
    The InputSystem is a 2nd layer of input reception
    that is for in-game players.
 */

public class InputSystem extends IteratingSystem
        implements InputProcessor, ControllerListener
{
    public HashMap<Controller, Integer> controllerMap;

    private HashMap<Integer, PlayerAction> keyMaps;
    private HashMap<Action, Boolean>[] controlStates;
    private OrthographicCamera camera;
    private final GameModeManager gmManager;

    public InputSystem(OrthographicCamera camera, GameModeManager gmManager)
    {
        super(Family.all(PlayerController.class).get());
        this.camera = camera;
        this.gmManager = gmManager;

        this.controllerMap = new HashMap<Controller, Integer>(4);

        keyMaps = new HashMap<Integer, PlayerAction>(Constants.NUM_OF_CONTROLS);

        keyMaps.put(Constants.P1_LEFT_KEY, new PlayerAction(1, Action.MOVING_LEFT));
        keyMaps.put(Constants.P1_RIGHT_KEY, new PlayerAction(1, Action.MOVING_RIGHT));
        keyMaps.put(Constants.P1_JUMP_KEY, new PlayerAction(1, Action.JUMPING));
        keyMaps.put(Constants.P1_ABILITY_1_KEY, new PlayerAction(1, Action.ABILITY_1));
        keyMaps.put(Constants.P1_ABILITY_2_KEY, new PlayerAction(1, Action.ABILITY_2));
        keyMaps.put(Constants.P1_ABILITY_3_KEY, new PlayerAction(1, Action.ABILITY_3));
        keyMaps.put(Constants.P1_START_KEY, new PlayerAction(1, Action.START));

        keyMaps.put(Constants.P2_LEFT_KEY, new PlayerAction(2, Action.MOVING_LEFT));
        keyMaps.put(Constants.P2_RIGHT_KEY, new PlayerAction(2, Action.MOVING_RIGHT));
        keyMaps.put(Constants.P2_JUMP_KEY, new PlayerAction(2, Action.JUMPING));
        keyMaps.put(Constants.P2_ABILITY_1_KEY, new PlayerAction(2, Action.ABILITY_1));
        keyMaps.put(Constants.P2_ABILITY_2_KEY, new PlayerAction(2, Action.ABILITY_2));
        keyMaps.put(Constants.P2_ABILITY_3_KEY, new PlayerAction(2, Action.ABILITY_3));
        keyMaps.put(Constants.P2_START_KEY, new PlayerAction(2, Action.START));

        keyMaps.put(Constants.P3_LEFT_KEY, new PlayerAction(3, Action.MOVING_LEFT));
        keyMaps.put(Constants.P3_RIGHT_KEY, new PlayerAction(3, Action.MOVING_RIGHT));
        keyMaps.put(Constants.P3_JUMP_KEY, new PlayerAction(3, Action.JUMPING));
        keyMaps.put(Constants.P3_ABILITY_1_KEY, new PlayerAction(3, Action.ABILITY_1));
        keyMaps.put(Constants.P3_ABILITY_2_KEY, new PlayerAction(3, Action.ABILITY_2));
        keyMaps.put(Constants.P3_ABILITY_3_KEY, new PlayerAction(3, Action.ABILITY_3));
        keyMaps.put(Constants.P3_START_KEY, new PlayerAction(3, Action.START));

        keyMaps.put(Constants.P4_LEFT_KEY, new PlayerAction(4, Action.MOVING_LEFT));
        keyMaps.put(Constants.P4_RIGHT_KEY, new PlayerAction(4, Action.MOVING_RIGHT));
        keyMaps.put(Constants.P4_JUMP_KEY, new PlayerAction(4, Action.JUMPING));
        keyMaps.put(Constants.P4_ABILITY_1_KEY, new PlayerAction(4, Action.ABILITY_1));
        keyMaps.put(Constants.P4_ABILITY_2_KEY, new PlayerAction(4, Action.ABILITY_2));
        keyMaps.put(Constants.P4_ABILITY_3_KEY, new PlayerAction(4, Action.ABILITY_3));
        keyMaps.put(Constants.P4_START_KEY, new PlayerAction(4, Action.START));

        controlStates = new HashMap[Constants.MAX_NUM_OF_PLAYERS];

        for (int i = 0; i < Constants.MAX_NUM_OF_PLAYERS; i++)
        {
            controlStates[i] = new HashMap<Action, Boolean>(Constants.NUM_OF_CONTROLS);

            controlStates[i].put(Action.MOVING_LEFT, false);
            controlStates[i].put(Action.MOVING_RIGHT, false);
            controlStates[i].put(Action.JUMPING, false);
            controlStates[i].put(Action.ABILITY_1, false);
            controlStates[i].put(Action.ABILITY_2, false);
            controlStates[i].put(Action.ABILITY_3, false);
            controlStates[i].put(Action.START, false);
        }
    }

//    @Override
//    public boolean checkProcessing()
//    {
//        return !gmManager.isGameOver();
//    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        PlayerController controller = Mapper.playerController.get(e);
        final int playerArrNum = controller.controllerNum;

        controller.previousUpdateActionMask = controller.actionMask;

        controller.setAction(Action.MOVING_LEFT,
                controlStates[playerArrNum].get(Action.MOVING_LEFT));
        controller.setAction(Action.MOVING_RIGHT,
                controlStates[playerArrNum].get(Action.MOVING_RIGHT));
        controller.setAction(Action.JUMPING,
                controlStates[playerArrNum].get(Action.JUMPING));
        controller.setAction(Action.ABILITY_1,
                controlStates[playerArrNum].get(Action.ABILITY_1));
        controller.setAction(Action.ABILITY_2,
                controlStates[playerArrNum].get(Action.ABILITY_2));
        controller.setAction(Action.ABILITY_3,
                controlStates[playerArrNum].get(Action.ABILITY_3));
        controller.setAction(Action.START,
                controlStates[playerArrNum].get(Action.START));
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
        final PlayerAction playerAction = keyMaps.get(keycode);

        if (playerAction != null)
            controlStates[playerAction.playerNum - 1].put(playerAction.action, state);
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("x: " + Constants.ConvertPixelsToMeters(screenX) + " y: " + Constants.ConvertPixelsToMeters(screenY));
        double rotation = Math.atan2(screenX, -screenY);

        System.out.println(rotation);
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
        camera.zoom = camera.zoom + (float)(amount) / 4;

        return false;
    }

    @Override
    public void connected(Controller controller)
    {
        System.out.println("CONTROLLER CONNECTED!");
    }

    @Override
    public void disconnected(Controller controller)
    {
        System.out.println("CONTROLLER DISCONNECTED!");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        final int curPlayer = controllerMap.get(controller);

        if (buttonCode == 0)
            controlStates[curPlayer].put(Action.JUMPING, true);
        else if (buttonCode == 2)
            controlStates[curPlayer].put(Action.ABILITY_1, true);
        else if (buttonCode == 3)
            controlStates[curPlayer].put(Action.ABILITY_2, true);
        else if (buttonCode == 1)
            controlStates[curPlayer].put(Action.ABILITY_3, true);
        // TODO: Add button code for start

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        final int curPlayer = controllerMap.get(controller);

        if (buttonCode == 0)
            controlStates[curPlayer].put(Action.JUMPING, false);
        else if (buttonCode == 2)
            controlStates[curPlayer].put(Action.ABILITY_1, false);
        else if (buttonCode == 3)
            controlStates[curPlayer].put(Action.ABILITY_2, false);
        else if (buttonCode == 1)
            controlStates[curPlayer].put(Action.ABILITY_3, false);

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        final int curPlayer = controllerMap.get(controller);
        
        if (axisCode == 1)
        {
            if (value < -.5f)
                controlStates[curPlayer].put(Action.MOVING_LEFT, true);
            else
                controlStates[curPlayer].put(Action.MOVING_LEFT, false);

            if (value > .5f)
                controlStates[curPlayer].put(Action.MOVING_RIGHT, true);
            else
                controlStates[curPlayer].put(Action.MOVING_RIGHT, false);
        }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    private class PlayerAction
    {
        public int playerNum;
        public Action action;

        public PlayerAction(int playerNum, Action action)
        {
            this.playerNum = playerNum;
            this.action = action;
        }
    }
}

