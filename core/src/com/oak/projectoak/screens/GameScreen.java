package com.oak.projectoak.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oak.projectoak.AbilityType;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Platformer;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.input.InputManager;
import com.oak.projectoak.physics.FootContactListenerManager;
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
    private Engine engine;
    private World b2world;
    OrthographicCamera camera;

    public GameScreen(Game game)
    {
        this.game = game;
        camera = new OrthographicCamera();

        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0;

        // Setup Entity engine
        engine = new Engine();
        EntityFactory.engine = engine;

        // Setup appropriate game setting
        DeathMatchManager deathMatchManager =
                new DeathMatchManager(engine, Constants.DEATHMATCH_NUM_TEAMS, Constants.DEATHMATCH_KILLS_TO_WIN);

        // Setup physics
        b2world = new World(Constants.GRAVITY, true);

        // Box2D Collisions setup
        GFContactListener contactListener = new GFContactListener();

        FootContactListenerManager footContactListenerManager = new FootContactListenerManager();
        contactListener.addContactListener(footContactListenerManager);
        engine.addEntityListener(Family.getFor(Platformer.class), footContactListenerManager);

        final ArenaRotationSystem arenaRotationSystem = new ArenaRotationSystem();

        PhysicsFactory.setWorld(b2world);
        final Entity trapRing = EntityFactory.createTrapRing(Constants.ARENA_CENTER, 0);

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
        engine.addSystem(new CameraZoomTransitionSystem(camera,
                (float) Constants.CAMERA_ZOOM_TO_RESOLUTION_SCALE /
                        ((float) Gdx.graphics.getHeight() - Constants.ZOOM_RING_PADDING)));

        engine.addSystem(new DynamicPhysicsSystem());
        engine.addSystem(new TrapPhysicsSystem());
        engine.addSystem(new RenderOffsetSystem());
        engine.addSystem(new GravitySystem(Constants.ARENA_CENTER));

        InputSystem inputSystem = new InputSystem(camera, deathMatchManager);
        inputManager.addInputProcessor(inputSystem);
        Controllers.addListener(inputSystem);
        engine.addSystem(inputSystem);

        engine.addSystem(new CameraSystem(camera, true));
        engine.addSystem(arenaRotationSystem);

        engine.addSystem(new PlayerMovementSystem(deathMatchManager));

        engine.addSystem(new AbilityCreationSystem(abilityDestructionSystem, deathMatchManager, trapRing));

        engine.addSystem(new PillarSystem(abilityDestructionSystem));

        engine.addSystem(new PhysicsDebugSystem(b2world, camera));
        engine.addSystem(new PhysicsStepSystem(b2world));
        engine.addSystem(abilitySystem);
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PlayerInvulnerableFlashingSystem());
        engine.addSystem(new UIEnergyUpdateSystem());

        SpriteBatch uiSpriteBatch = new SpriteBatch();

        engine.addSystem(new RenderSystem(camera, "background"));
        engine.addSystem(new SpriteBatchStarter(uiSpriteBatch));
        engine.addSystem(new UIRenderSystem(uiSpriteBatch));
        engine.addSystem(new TextRenderSystem(uiSpriteBatch));
        engine.addSystem(new SpriteBatchEnder(uiSpriteBatch));
        engine.addSystem(new GraphicsDebugSystem(camera));

        engine.addSystem(playerDestructionSystem);
        engine.addSystem(abilityDestructionSystem);

        engine.addSystem(new GameOverSystem(deathMatchManager, this, game, camera));

        EntityFactory.createArenaCircle(Constants.ARENA_CENTER);

        final Entity player1 = EntityFactory.createPlayer(0, (float) Math.PI, true, 0, Constants.P1_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player1.getComponent(Platformer.class), true);

        final Entity player2 = EntityFactory.createPlayer(1, 0, false, 1, Constants.P2_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player2.getComponent(Platformer.class), false);

        final Entity player3 = EntityFactory.createPlayer(2, (float) Math.PI * 3 / 2, false, 0, Constants.P3_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player3.getComponent(Platformer.class), false);

        final Entity player4 = EntityFactory.createPlayer(3, (float) Math.PI / 2, true, 1, Constants.P4_UI_POSITION.cpy(),
                new AbilityType[]{AbilityType.STAKE, AbilityType.PILLAR, AbilityType.LIGHTNING_BOLT});
        abilityDestructionSystem.addFootContactUser(player4.getComponent(Platformer.class), true);

        Array<Controller> controllers = Controllers.getControllers();
        for (int i = 0; i < controllers.size; i++)
        {
            inputSystem.controllerMap.put(controllers.get(i), i);
        }

        AssetLoader.playMusic();
    }

	@Override
	public void dispose()
    {
        b2world.dispose();
        ImmutableArray<EntitySystem> systems = engine.getSystems();
        for (int i = 0; i < systems.size(); i++)
        {
            engine.removeSystem(systems.get(i));
        }

        engine = null;
    }

	@Override
	public void render(float delta)
    {
//        camera.rotate((float)Math.random());
//        camera.zoom = (float)Math.random() + 1f;
//		Gdx.gl.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
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
