package com.blakeandshahan.cirque.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.blakeandshahan.cirque.Action;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Platformer;
import com.blakeandshahan.cirque.components.PlayerController;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;
import com.blakeandshahan.cirque.screens.GameScreen;
import com.blakeandshahan.cirque.systems.ability.AbilityDestructionSystem;

public class GameOverSystem extends IteratingSystem implements InputProcessor
{
    private final GameModeManager gmManager;
    private final GameScreen gameScreen;
    private final Game game;
    private final OrthographicCamera camera;
    private final AbilityDestructionSystem abilityDestructionSystem;

    private boolean hasRun;

    public GameOverSystem(GameModeManager gmManager, GameScreen gameScreen, Game game, OrthographicCamera camera,
                          AbilityDestructionSystem abilityDestructionSystem)
    {
        super(Family.getFor(PlayerController.class));
        this.gmManager = gmManager;
        this.gameScreen = gameScreen;
        this.game = game;
        this.camera = camera;
        this.abilityDestructionSystem = abilityDestructionSystem;

        hasRun = false;
    }

    //SHOULD WE DO RUN processSystem()??
    @Override
    public boolean checkProcessing()
    {
        return !hasRun && gmManager.isGameOver();
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        final PlayerController controller = Mapper.playerController.get(e);

        if (controller.isActionOn(Action.START))
        {
            if (!controller.startButtonHeld)
            {
                // TODO: If we have enough players:
                // 3. Reset positions to defaults.
                // 4. Set energy to original levels.
                // 5. Setup scores.

                // If we haven't initialized this player...
                if (!Mapper.player.has(e))
                {
                    switch (controller.controllerNum)
                    {
                        case 0:
                            EntityFactory.createPlayerFromController(
                                    e, (float) Math.PI, true, 0, Constants.P1_UI_POSITION.cpy(), null);
                            abilityDestructionSystem.addFootContactUser(e.getComponent(Platformer.class), true);
                            break;
                        case 1:
                            EntityFactory.createPlayerFromController(e, 0, false, 1, Constants.P2_UI_POSITION.cpy(), null);
                            abilityDestructionSystem.addFootContactUser(e.getComponent(Platformer.class), false);
                            break;
                        case 2:
                            EntityFactory.createPlayerFromController(
                                    e, (float) Math.PI * 3 / 2, false, 0, Constants.P3_UI_POSITION.cpy(), null);
                            abilityDestructionSystem.addFootContactUser(e.getComponent(Platformer.class), false);
                            break;
                        case 3:
                            EntityFactory.createPlayerFromController(
                                    e, (float) Math.PI / 2, true, 1, Constants.P4_UI_POSITION.cpy(), null);
                            abilityDestructionSystem.addFootContactUser(e.getComponent(Platformer.class), true);
                            break;
                    }
                }
                else
                {
                    controller.readyToBegin = true;

                    boolean allPlayersReady = true;
                    int numOfPlayersReady = 0;
                    ImmutableArray<Entity> controllerEntities = getEntities();
                    for (int i = 0; i < controllerEntities.size(); i++)
                    {
                        if (Mapper.player.has(controllerEntities.get(i)))
                        {
                            numOfPlayersReady++;

                            if (!Mapper.playerController.get(controllerEntities.get(i)).readyToBegin)
                            {
                                allPlayersReady = false;
                                break;
                            }
                        }
                    }

                    if (allPlayersReady && numOfPlayersReady % 2 == 0)
                    {
                        EntityFactory.engine.addSystem(new CameraZoomTransitionSystem(
                                CameraZoomTransitionSystem.TransitionType.RESTART, camera, 0, gmManager));
                    }
                }

                controller.startButtonHeld = true;
            }
        }
        else
        {
            controller.startButtonHeld = false;
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
//        if (keycode == Input.Keys.ENTER)
//        {
//            EntityFactory.engine.addSystem(new CameraZoomTransitionSystem(camera, 0, game, gameScreen, true));
//        }
//        else if (keycode == Input.Keys.ESCAPE)
//        {
//            EntityFactory.engine.addSystem(new CameraZoomTransitionSystem(camera, 0, game, gameScreen, false));
//        }

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
