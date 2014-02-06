package com.oak.projectoak.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.input.InputManager;
import com.oak.projectoak.physics.*;
import com.oak.projectoak.systems.PlatformerSystem;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.systems.*;
import com.oak.projectoak.systems.InputSystem;
import com.oak.projectoak.systems.physics.*;

/*
    The GameScreen is screen that contains the actual game.
    It initializes all the moving pieces of the game and
    runs everything in render().
 */

public class GameScreen implements Screen
{
    private World world;
    private final OrthographicCamera camera;

    public GameScreen()
    {
        camera = new OrthographicCamera();

        // Setup asset loading
        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = Constants.INITIAL_CAMERA_ZOOM;

        // Setup Entity world
        world = new World();

        // Setup physics
        com.badlogic.gdx.physics.box2d.World b2world =
                new com.badlogic.gdx.physics.box2d.World(Constants.GRAVITY, true);

        // Setup input manager
        final InputManager inputManager = new InputManager(world);

        // Setup systems
        world.setSystem(new DynamicPhysicsSystem());
        world.setSystem(new ExternalGravitySystem(Constants.ARENA_CENTER));
        world.setSystem(new InternalGravitySystem(Constants.ARENA_CENTER));

        PlatformerSystem platformerSystem = new PlatformerSystem();
        b2world.setContactListener(platformerSystem);

        world.setSystem(platformerSystem);

        InputSystem input = new InputSystem(camera);
        inputManager.addInputProcessor(input);
        world.setSystem(input);

        world.setSystem(new CameraSystem(camera, true));

        world.setSystem(new PlayerMovementSystem());
        world.setSystem(new AbilitySystem());
        world.setSystem(new PhysicsDebugSystem(b2world, camera));
        world.setSystem(new PhysicsStepSystem(b2world));
        world.setSystem(new AnimationSystem());
        world.setSystem(new RenderSystem(camera));

        world.setManager(new GroupManager());

        world.setDelta(.01f);
        world.initialize();

        PhysicsFactory.setWorld(b2world);

        EntityFactory.createExternalPlayer(world, 0, 1);
        EntityFactory.createExternalPlayer(world, (float)Math.PI, 1);
        EntityFactory.createInternalPlayer(world, (float)Math.PI / 2, 1);
        EntityFactory.createInternalPlayer(world, (float)Math.PI * 3 / 2, 1);
        EntityFactory.createArenaCircle(world, Constants.ARENA_CENTER);

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hide()
    {
        //To change body of implemented methods use File | Settings | File Templates.
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
