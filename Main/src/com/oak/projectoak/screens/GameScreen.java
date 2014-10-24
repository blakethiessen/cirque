package com.oak.projectoak.screens;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Game;
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
import com.oak.projectoak.systems.ability.PillarSystem;
import com.oak.projectoak.systems.physics.*;
import com.oak.projectoak.systems.render.*;

/*
    The GameScreen is screen that contains the actual game.
    It initializes all the moving pieces of the game and
    runs everything in render().
 */

public class GameScreen implements Screen
{
    private final Game game;
    private World world;
    private com.badlogic.gdx.physics.box2d.World b2world;
    OrthographicCamera camera;

    public GameScreen(Game game)
    {
        this.game = game;
        camera = new OrthographicCamera();

        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0;

        // Setup Entity world
        world = new World();

        // Setup appropriate game setting
        DeathMatchManager deathMatchManager =
                new DeathMatchManager(world, Constants.DEATHMATCH_NUM_TEAMS, Constants.DEATHMATCH_KILLS_TO_WIN);

        // Setup physics
        b2world = new com.badlogic.gdx.physics.box2d.World(Constants.GRAVITY, true);

        // Box2D Collisions setup
        GFContactListener contactListener = new GFContactListener();

        final FootContactListenerSystem footContactListenerSystem = new FootContactListenerSystem();
        contactListener.addContactListener(footContactListenerSystem);

        final ArenaRotationSystem arenaRotationSystem = new ArenaRotationSystem();

        PhysicsFactory.setWorld(b2world);
        final Entity trapRing = EntityFactory.createTrapRing(world, Constants.ARENA_CENTER, 0);

        final PlayerDestructionSystem playerDestructionSystem =
                new PlayerDestructionSystem(b2world, deathMatchManager, Constants.RESPAWN_TIME_SEC, arenaRotationSystem);
        final AbilityDestructionSystem abilityDestructionSystem = new AbilityDestructionSystem(b2world);

        final AbilitySystem abilitySystem =
                new AbilitySystem(playerDestructionSystem, abilityDestructionSystem, deathMatchManager);
        contactListener.addContactListener(abilitySystem);

        b2world.setContactListener(contactListener);

        // Setup input manager
        final InputManager inputManager = new InputManager();

        // Setup systems
        world.setSystem(new CameraZoomTransitionSystem(camera,
                (float) Constants.CAMERA_ZOOM_TO_RESOLUTION_SCALE /
                        ((float) Gdx.graphics.getHeight() - Constants.ZOOM_RING_PADDING)));

        world.setSystem(new DynamicPhysicsSystem());
        world.setSystem(new TrapPhysicsSystem());
        world.setSystem(new RenderOffsetSystem());
        world.setSystem(new GravitySystem(Constants.ARENA_CENTER));

        InputSystem inputSystem = new InputSystem(camera, deathMatchManager);
        inputManager.addInputProcessor(inputSystem);
        Controllers.addListener(inputSystem);
        world.setSystem(inputSystem);

        world.setSystem(new CameraSystem(camera, true));
        world.setSystem(arenaRotationSystem);

        world.setSystem(footContactListenerSystem);
        world.setSystem(new PlayerMovementSystem(deathMatchManager));

        world.setSystem(new AbilityCreationSystem(abilityDestructionSystem, deathMatchManager, trapRing));

        world.setSystem(new PillarSystem(abilityDestructionSystem));

        world.setSystem(new PhysicsDebugSystem(b2world, camera));
        world.setSystem(new PhysicsStepSystem(b2world));
        world.setSystem(abilitySystem);
        world.setSystem(new AnimationSystem());
        world.setSystem(new PlayerInvulnerableFlashingSystem());
        world.setSystem(new UIEnergyUpdateSystem());

        SpriteBatch uiSpriteBatch = new SpriteBatch();

        world.setSystem(new RenderSystem(camera, "background"));
        world.setSystem(new SpriteBatchStarter(uiSpriteBatch));
        world.setSystem(new UIRenderSystem(uiSpriteBatch));
        world.setSystem(new TextRenderSystem(uiSpriteBatch));
        world.setSystem(new SpriteBatchEnder(uiSpriteBatch));
        world.setSystem(new GraphicsDebugSystem(camera));

        world.setSystem(playerDestructionSystem);
        world.setSystem(abilityDestructionSystem);

        world.setSystem(new GameOverSystem(deathMatchManager, this, game, camera));

        world.setManager(new GroupManager());

        world.setDelta(.01f);
        world.initialize();

        EntityFactory.createArenaCircle(world, Constants.ARENA_CENTER);

        final Entity player1 = EntityFactory.createPlayer(world, 0, 0, true, 0, Constants.P1_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player1.getComponent(Platformer.class), true);

        final Entity player2 = EntityFactory.createPlayer(world, 1, (float) Math.PI, false, 1, Constants.P2_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player2.getComponent(Platformer.class), false);

//        final Entity player3 = EntityFactory.createPlayer(world, 2, (float) Math.PI / 2, true, 1, Constants.P3_UI_POSITION.cpy(),
//                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
//        abilityDestructionSystem.addFootContactUser(player3.getComponent(Platformer.class), true);
//
//        final Entity player4 = EntityFactory.createPlayer(world, 3, (float) Math.PI * 3 / 2, false, 0, Constants.P4_UI_POSITION.cpy(),
//                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
//        abilityDestructionSystem.addFootContactUser(player4.getComponent(Platformer.class), false);

        Array<Controller> controllers = Controllers.getControllers();
        for (int i = 0; i < controllers.size; i++)
        {
            inputSystem.controllerMap.put(controllers.get(i), i);
        }
    }

	@Override
	public void dispose()
    {
        b2world.dispose();
        ImmutableBag<EntitySystem> systems = world.getSystems();
        for (int i = 0; i < systems.size(); i++)
        {
            world.deleteSystem(systems.get(i));
        }

        world = null;
    }

	@Override
	public void render(float delta)
    {
//        camera.rotate((float)Math.random());
//        camera.zoom = (float)Math.random() + 1f;
//		Gdx.gl.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
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
