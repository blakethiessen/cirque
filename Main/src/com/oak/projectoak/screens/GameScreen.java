package com.oak.projectoak.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.input.InputManager;
import com.oak.projectoak.physics.PhysicsFactory;
import com.oak.projectoak.physics.contactlisteners.GFContactListener;
import com.oak.projectoak.systems.*;
import com.oak.projectoak.systems.ability.AbilityCreationSystem;
import com.oak.projectoak.systems.ability.AbilityDestructionSystem;
import com.oak.projectoak.systems.ability.AbilitySystem;
import com.oak.projectoak.systems.physics.*;
import gamemodemanagers.DeathMatchManager;

/*
    The GameScreen is screen that contains the actual game.
    It initializes all the moving pieces of the game and
    runs everything in render().
 */

public class GameScreen implements Screen
{
    private World world;

    public GameScreen()
    {
        OrthographicCamera camera = new OrthographicCamera();

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

        world.setSystem(new DynamicPhysicsSystem());
        world.setSystem(new RenderOffsetSystem());
        world.setSystem(new GravitySystem(Constants.ARENA_CENTER));

        InputSystem input = new InputSystem(camera, deathMatchManager);
        inputManager.addInputProcessor(input);
        world.setSystem(input);

        world.setSystem(new CameraSystem(camera, true));
        world.setSystem(arenaRotationSystem);

        world.setSystem(footContactListenerSystem);
        world.setSystem(new PlayerMovementSystem());
        world.setSystem(new AbilityCreationSystem(world));
        world.setSystem(abilitySystem);
        world.setSystem(new PhysicsDebugSystem(b2world, camera));
        world.setSystem(new PhysicsStepSystem(b2world));
        world.setSystem(new AnimationSystem());
        world.setSystem(new PlayerInvulnerableFlashingSystem());
        world.setSystem(new UIEnergyUpdateSystem());
        world.setSystem(new RenderSystem(camera));
        world.setSystem(new TextRenderSystem());
        world.setSystem(new GraphicsDebugSystem(camera));

        world.setManager(new GroupManager());

        world.setDelta(.01f);
        world.initialize();

        PhysicsFactory.setWorld(b2world);

        EntityFactory.createArenaCircle(world, Constants.ARENA_CENTER);

        EntityFactory.createPlayer(world, 0, true, 0, new Vector2(10, 200));
        EntityFactory.createPlayer(world, (float) Math.PI, false, 1, new Vector2(10, 180));
//        EntityFactory.createPlayer(world, (float)Math.PI / 2, true, 0, new Vector2(10, 160));
//        EntityFactory.createPlayer(world, (float) Math.PI * 3 / 2, false, 1, new Vector2(10, 140));

        for(Controller controller: Controllers.getControllers())
        {
            Gdx.app.log("Controller", controller.getName());
        }
    }

	@Override
	public void dispose()
    {
	}

	@Override
	public void render(float delta)
    {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
