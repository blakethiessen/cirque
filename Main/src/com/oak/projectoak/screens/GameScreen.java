package com.oak.projectoak.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.oak.projectoak.AbilityType;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.input.InputManager;
import com.oak.projectoak.physics.PhysicsFactory;
import com.oak.projectoak.physics.contactlisteners.GFContactListener;
import com.oak.projectoak.systems.*;
import com.oak.projectoak.systems.ability.AbilityCreationSystem;
import com.oak.projectoak.systems.ability.AbilityDestructionSystem;
import com.oak.projectoak.systems.ability.AbilitySystem;
import com.oak.projectoak.systems.physics.*;
import com.oak.projectoak.systems.render.*;

/*
    The GameScreen is screen that contains the actual game.
    It initializes all the moving pieces of the game and
    runs everything in render().
 */

public class GameScreen implements Screen
{
    private World world;
    OrthographicCamera camera;

    public GameScreen()
    {
        camera = new OrthographicCamera();

        // Setup asset loading
        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = Constants.INITIAL_CAMERA_ZOOM;

        // Setup Entity world
        world = new World();

        // Setup appropriate game setting
        DeathMatchManager deathMatchManager = new DeathMatchManager(world, Constants.DEATHMATCH_NUM_TEAMS, Constants.DEATHMATCH_KILLS_TO_WIN);

        // Setup physics
        com.badlogic.gdx.physics.box2d.World b2world =
                new com.badlogic.gdx.physics.box2d.World(Constants.GRAVITY, true);

        // Box2D Collisions setup
        GFContactListener contactListener = new GFContactListener();

        final FootContactListenerSystem footContactListenerSystem = new FootContactListenerSystem();
        contactListener.addContactListener(footContactListenerSystem);

        final ArenaRotationSystem arenaRotationSystem = new ArenaRotationSystem();

        final PlayerDestructionSystem playerDestructionSystem = new PlayerDestructionSystem(b2world, deathMatchManager, Constants.RESPAWN_TIME_SEC, arenaRotationSystem);
        final AbilityDestructionSystem abilityDestructionSystem = new AbilityDestructionSystem(b2world);

        final AbilitySystem abilitySystem = new AbilitySystem(playerDestructionSystem, abilityDestructionSystem, deathMatchManager);
        contactListener.addContactListener(abilitySystem);

        b2world.setContactListener(contactListener);

        // Setup input manager
        final InputManager inputManager = new InputManager(world);

        // Setup systems
        world.setSystem(playerDestructionSystem);
        world.setSystem(abilityDestructionSystem);

        PhysicsFactory.setWorld(b2world);
        final Entity trapRing = EntityFactory.createTrapRing(world, Constants.ARENA_CENTER);

        world.setSystem(new DynamicPhysicsSystem());
        world.setSystem(new TrapPhysicsSystem());
        world.setSystem(new RenderOffsetSystem());
        world.setSystem(new GravitySystem(Constants.ARENA_CENTER));

        InputSystem input = new InputSystem(camera, deathMatchManager);
        inputManager.addInputProcessor(input);
        Controllers.addListener(input);
        world.setSystem(input);

        world.setSystem(new CameraSystem(camera, true));
        world.setSystem(arenaRotationSystem);

        world.setSystem(footContactListenerSystem);
        world.setSystem(new PlayerMovementSystem());

        world.setSystem(new AbilityCreationSystem(abilityDestructionSystem, trapRing));

        world.setSystem(abilitySystem);
        world.setSystem(new PhysicsDebugSystem(b2world, camera));
        world.setSystem(new PhysicsStepSystem(b2world));
        world.setSystem(new AnimationSystem());
        world.setSystem(new PlayerInvulnerableFlashingSystem());
        world.setSystem(new UIEnergyUpdateSystem());

        SpriteBatch uiSpriteBatch = new SpriteBatch();

        world.setSystem(new RenderSystem(camera));
        world.setSystem(new SpriteBatchStarter(uiSpriteBatch));
        world.setSystem(new UIRenderSystem(uiSpriteBatch));
        world.setSystem(new TextRenderSystem(uiSpriteBatch));
        world.setSystem(new SpriteBatchEnder(uiSpriteBatch));
        world.setSystem(new GraphicsDebugSystem(camera));

        world.setManager(new GroupManager());

        world.setDelta(.01f);
        world.initialize();

        EntityFactory.createArenaCircle(world, Constants.ARENA_CENTER);

        final Entity player1 = EntityFactory.createPlayer(world, 0, 0, true, 0, Constants.P1_UI_POSITION,
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.STAKE});
        abilityDestructionSystem.addFootContactUser(player1.getComponent(Platformer.class), true);

        final Entity player2 = EntityFactory.createPlayer(world, 1, (float) Math.PI, false, 1, Constants.P2_UI_POSITION,
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.STAKE});
        abilityDestructionSystem.addFootContactUser(player2.getComponent(Platformer.class), false);

        final Entity player3 = EntityFactory.createPlayer(world, 2, (float) Math.PI / 2, true, 0, Constants.P3_UI_POSITION,
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.STAKE});
        abilityDestructionSystem.addFootContactUser(player3.getComponent(Platformer.class), true);

        final Entity player4 = EntityFactory.createPlayer(world, 3, (float) Math.PI * 3 / 2, false, 1, Constants.P4_UI_POSITION,
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.STAKE});
        abilityDestructionSystem.addFootContactUser(player4.getComponent(Platformer.class), false);

        Array<Controller> controllers = Controllers.getControllers();
        for(int i = 0; i < controllers.size; i++)
        {
            input.controllerMap.put(controllers.get(i), i);
        }
    }

	@Override
	public void dispose()
    {
	}

	@Override
	public void render(float delta)
    {
//        camera.rotate((float)Math.random());
//        camera.zoom = (float)Math.random() + 1f;
//		Gdx.gl.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
        Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        world.process();
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
}
