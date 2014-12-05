package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.AbilityCreation;
import com.blakeandshahan.cirque.components.AbilityUser;
import com.blakeandshahan.cirque.components.ArenaRotation;
import com.blakeandshahan.cirque.components.Player;
import com.blakeandshahan.cirque.entity.EntityFactory;
import com.blakeandshahan.cirque.gamemodemanagers.GameModeManager;
import javafx.util.Pair;

public class CameraZoomTransitionSystem extends EntitySystem
{
    private final OrthographicCamera camera;
    private float finalCameraZoom;
    private final GameModeManager gmManager;
    private final PlayerDestructionSystem playerDestructionSystem;

    private TransitionType transitionType;

    private float zoomVel;
    private boolean zoomingInwards;

    public CameraZoomTransitionSystem(TransitionType transitionType, OrthographicCamera camera, float finalCameraZoom,
                                      GameModeManager gmManager, PlayerDestructionSystem playerDestructionSystem)
    {
        this.camera = camera;
        this.finalCameraZoom = finalCameraZoom;
        this.gmManager = gmManager;
        this.playerDestructionSystem = playerDestructionSystem;
        zoomVel = Constants.CAMERA_TRANSITION_ZOOM_ACCEL;

        if (finalCameraZoom < camera.zoom)
            zoomingInwards = true;
        else
            zoomingInwards = false;

        this.transitionType = transitionType;
    }

    @Override
    public void update(float deltaTime)
    {
        if (zoomingInwards)
            camera.zoom -= zoomVel;
        else
            camera.zoom += zoomVel;

        if ((!zoomingInwards && camera.zoom >= finalCameraZoom) ||
                (zoomingInwards && camera.zoom < finalCameraZoom))
        {
            switch (transitionType)
            {
                case INITIAL_LOAD:
                    EntityFactory.engine.removeSystem(this);
                    break;
                case RESTART:
                    if (zoomingInwards)
                    {
                        playerDestructionSystem.respawnDeadPlayers();

                        // Reset arena rotation.
                        ImmutableArray<Entity> arenaEntities =
                                EntityFactory.engine.getEntitiesFor(Family.getFor(ArenaRotation.class));
                        for (int i = 0; i < arenaEntities.size(); i++)
                        {
                            Entity e = arenaEntities.get(i);
                            Mapper.arenaRotation.get(e).rotationVelocity = 0;
                            Mapper.dynamicPhysics.get(e).body.setAngularVelocity(0);
                        }

                        ImmutableArray<Entity> playerEntities =
                                EntityFactory.engine.getEntitiesFor(Family.getFor(Player.class));
                        for (int i = 0; i < playerEntities.size(); i++)
                        {
                            Entity e = playerEntities.get(i);
                            // Add abilities/reset abilities
                            AbilityUser abilityUser = Mapper.abilityUser.get(e);
                            if (abilityUser == null)
                                EntityFactory.addAbilities(e, Constants.DEFAULT_ABILITIES);
                            else
                            {
                                for (AbilityCreation abilityCreation : abilityUser.abilities)
                                    abilityCreation.resetAbility();
                            }

                            // Move players back to spawn points.
                            Pair<Float, Boolean> spawnPosition = Constants.PLAYER_RADIAL_SPAWN_POSITION.get(i);
                            Vector2 twoDSpawnPosition = Constants.ConvertRadialTo2DPosition(
                                    spawnPosition.getKey(), spawnPosition.getValue());
                            Mapper.dynamicPhysics.get(e).body.setTransform(twoDSpawnPosition, 0);

                            // Reset readyToBegin flags.
                            Mapper.playerController.get(e).readyToBegin = false;
                        }

                        gmManager.resetGame();

                        zoomVel = Constants.CAMERA_TRANSITION_ZOOM_ACCEL;
                        zoomingInwards = false;
                        finalCameraZoom = Constants.MIN_CAMERA_ZOOM;
                    }
                    else
                    {
                        EntityFactory.engine.removeSystem(this);
                    }
                    break;
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

    public enum TransitionType
    {
        INITIAL_LOAD,
        RESTART
    }
}
