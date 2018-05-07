package com.blakeandshahan.cirque.systems;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.blakeandshahan.cirque.Action;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.PlayerController;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;
import com.blakeandshahan.cirque.systems.ability.AbilityDestructionSystem;

import java.util.ArrayList;

public class GameOverSystem extends IteratingSystem
{
    private final GameModeManager gmManager;
    private final OrthographicCamera camera;
    private final PlayerDestructionSystem playerDestructionSystem;
    private final AbilityDestructionSystem abilityDestructionSystem;

    private boolean hasRun;

    public GameOverSystem(GameModeManager gmManager, OrthographicCamera camera,
                          PlayerDestructionSystem playerDestructionSystem,
                          AbilityDestructionSystem abilityDestructionSystem)
    {
        super(Family.all(PlayerController.class).get());
        this.gmManager = gmManager;
        this.camera = camera;
        this.playerDestructionSystem = playerDestructionSystem;
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

        if (controller.actionDownOnce(Action.START))
        {
            // If we haven't initialized this player, add him/her.
            if (!Mapper.player.has(e))
            {
                switch (controller.controllerNum)
                {
                    case 0:
                        EntityFactory.createPlayerFromController(e, (float) Math.PI, true, 0,
                                Constants.P1_UI_POSITION.cpy(), null, abilityDestructionSystem);
                        break;
                    case 1:
                        EntityFactory.createPlayerFromController(e, 0, false, 1,
                                Constants.P2_UI_POSITION.cpy(), null, abilityDestructionSystem);
                        break;
                    case 2:
                        EntityFactory.createPlayerFromController(e, (float) Math.PI * 3 / 2, true, 0,
                                Constants.P3_UI_POSITION.cpy(), null, abilityDestructionSystem);
                        break;
                    case 3:
                        EntityFactory.createPlayerFromController(e, (float) Math.PI / 2, false, 1,
                                Constants.P4_UI_POSITION.cpy(), null, abilityDestructionSystem);
                        break;
                }
            }
            // Otherwise, we've added the player and we want to remove him/her.
            else
            {
                ImmutableArray<Component> components = e.getComponents();
                ArrayList<Class<? extends Component>> componentsToRemove = new ArrayList<>();
                for (int i = 0; i < components.size(); i++)
                {
                    Component curComponent = components.get(i);
                    Class<? extends Component> curComponentClass = curComponent.getClass();
                    // Remove everything but the controller.
                    if (!curComponentClass.equals(PlayerController.class))
                    {
                        componentsToRemove.add(curComponentClass);
                    }
                }

                for (Class<? extends Component> component : componentsToRemove) {
                    e.remove(component);
                }
            }
        }

        if (controller.actionDownOnce(Action.ABILITY_1) && Mapper.player.has(e))
        {
            controller.readyToBegin = true;

            // Check to see if all players are ready.
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
                // Transition to the game.
                EntityFactory.engine.addSystem(new CameraZoomTransitionSystem(
                        CameraZoomTransitionSystem.TransitionType.RESTART, camera,
                        0, gmManager, playerDestructionSystem));
            }
        }
    }
}
