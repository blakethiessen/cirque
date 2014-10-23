package com.oak.projectoak.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.oak.projectoak.Action;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.gamemodemanagers.DeathMatchManager;
import com.oak.projectoak.input.InputManager;
import com.oak.projectoak.physics.PhysicsFactory;
import com.oak.projectoak.physics.contactlisteners.GFContactListener;
import com.oak.projectoak.systems.*;
import com.oak.projectoak.systems.physics.*;
import com.oak.projectoak.systems.render.*;

public class TitleScreen implements Screen, InputProcessor
{
    private final Game game;
    private final int BACKGROUND_X;
    private final int BACKGROUND_Y;
    OrthographicCamera camera;


    World world;
    com.badlogic.gdx.physics.box2d.World b2world;
    SpriteBatch batch;
    Sprite background;
    Sprite arenaRing;
    Sprite logo;
    InputSystem inputSystem;

    boolean readyToStartGame = false;
    private float rotationSpeed = .1f;
    private float zoomIncreasePerFrame = .002f;
    private float logoAlpha = 1;
    private int totalPlayers;
    private Entity e0, e1, e2, e3;
    private Entity por0, por1, por2, por3;


    public TitleScreen(Game game)
    {
//        this.game = game;
//        camera = new OrthographicCamera();
        batch = new SpriteBatch();
//
//        // Setup asset loading
        AssetLoader.initialize();
//
//        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        camera.zoom = 1.36f;
////        camera.zoom = (float)Constants.CAMERA_ZOOM_TO_RESOLUTION_SCALE / ((float)Gdx.graphics.getHeight());
//
        background = new Sprite(AssetLoader.getTextureRegion("background"));

        arenaRing = new Sprite(AssetLoader.getTextureRegion(Constants.OUTER_RING));
        arenaRing.setPosition((Gdx.graphics.getWidth() - arenaRing.getWidth()) / 2, (Gdx.graphics.getHeight() - arenaRing.getHeight()) / 2);

        logo = new Sprite(AssetLoader.getTextureRegion(Constants.TITLE_LOGO));
        logo.setPosition((Gdx.graphics.getWidth() - logo.getWidth()) / 2, (Gdx.graphics.getHeight() - logo.getHeight()) / 2);

//        Gdx.input.setInputProcessor(this);

        BACKGROUND_X = (Gdx.graphics.getWidth() - background.getRegionWidth()) / 2;
        BACKGROUND_Y = (Gdx.graphics.getHeight() - background.getRegionHeight()) / 2;


        //PASTE
        totalPlayers = 0;
        this.game = game;
        camera = new OrthographicCamera();

        AssetLoader.initialize();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0;

        // Setup Entity world
        world = new World();

        // Setup appropriate game setting
        DeathMatchManager deathMatchManager = new DeathMatchManager(world, Constants.DEATHMATCH_NUM_TEAMS, Constants.DEATHMATCH_KILLS_TO_WIN);

        // Setup physics
        b2world = new com.badlogic.gdx.physics.box2d.World(Constants.GRAVITY, true);

        // Box2D Collisions setup
        GFContactListener contactListener = new GFContactListener();

        final FootContactListenerSystem footContactListenerSystem = new FootContactListenerSystem();
        contactListener.addContactListener(footContactListenerSystem);

        //final ArenaRotationSystem arenaRotationSystem = new ArenaRotationSystem();

        PhysicsFactory.setWorld(b2world);
        final Entity trapRing = EntityFactory.createTrapRing(world, Constants.ARENA_CENTER, 0);


        b2world.setContactListener(contactListener);

        // Setup input manager
        final InputManager inputManager = new InputManager();

        // Setup systems
        world.setSystem(new CameraZoomTransitionSystem(camera, (float) Constants.CAMERA_ZOOM_TO_RESOLUTION_SCALE / ((float) Gdx.graphics.getHeight() - Constants.ZOOM_RING_PADDING)));
        world.setSystem(new DynamicPhysicsSystem());
        world.setSystem(new TrapPhysicsSystem());
        world.setSystem(new RenderOffsetSystem());
        world.setSystem(new GravitySystem(Constants.ARENA_CENTER));


        inputSystem = new InputSystem(camera, deathMatchManager);
        inputManager.addInputProcessor(inputSystem);
        Controllers.addListener(inputSystem);


        world.setSystem(inputSystem);
        world.setSystem(new CameraSystem(camera, true));
        //world.setSystem(arenaRotationSystem);
        world.setSystem(footContactListenerSystem);
        world.setSystem(new PlayerMovementSystem(deathMatchManager));

        //world.setSystem(new AbilityCreationSystem(abilityDestructionSystem, deathMatchManager, trapRing));


        world.setSystem(new PhysicsDebugSystem(b2world, camera));
        world.setSystem(new PhysicsStepSystem(b2world));
        //world.setSystem(abilitySystem);
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
        //world.setSystem(new ScoreTrackingSystem(deathMatchManager, this, game, camera,2,2));    //2 teams with 2 players each);

        //world.setSystem(playerDestructionSystem);
        //world.setSystem(abilityDestructionSystem);

        world.setManager(new GroupManager());

        world.setDelta(.01f);
        world.initialize();

        EntityFactory.createArenaCircle(world, Constants.ARENA_CENTER);



        e0 = EntityFactory.createPlayer_ControllerOnly(world, 0);
        e1 = EntityFactory.createPlayer_ControllerOnly(world, 1);
        e2 = EntityFactory.createPlayer_ControllerOnly(world, 2);
        e3 = EntityFactory.createPlayer_ControllerOnly(world, 3);

        Array<Controller> controllers = Controllers.getControllers();
        for (int i = 0; i < controllers.size; i++)
        {
            inputSystem.controllerMap.put(controllers.get(i), i);
        }




        AssetLoader.playMusic();
    }

//    @Override
//    public void render(float delta)
//    {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//
//        camera.update();
//
//        if (readyToStartGame)
//        {
//            beginGame();
//        }
//        else
//        {
//            camera.rotate(rotationSpeed);
//            logo.rotate(-rotationSpeed);
//        }
//
//
//        draw();
//    }


    @Override
    public void render(float delta)
    {
//        camera.rotate((float)Math.random());
//        camera.zoom = (float)Math.random() + 1f;
//		Gdx.gl.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            camera.rotate(rotationSpeed);
            logo.rotate(-rotationSpeed);
        draw();

        world.process();
        titleScreenProcess();
    }



    private void titleScreenProcess()
    {
        Player p0, p1, p2, p3;

        p0 = e0.getComponent(Player.class);
        p1 = e1.getComponent(Player.class);
        p2 = e2.getComponent(Player.class);
        p3 = e3.getComponent(Player.class);

        if(p0.isActionOn(Action.ABILITY_1) && (totalPlayers == 2 || totalPlayers == 4))
            beginGame();

        addOrRemovePlayers(p0, p1, p2, p3);
    }


    private void addOrRemovePlayers(Player p0, Player p1, Player p2, Player p3)
    {
        //Add player stuffs
        if(totalPlayers < 4)
        {
            if     (!p0.havePlayerAttributesBeenSet() && p0.isActionOn(Action.JUMPING))
            {
               upgradePlayer(e0, totalPlayers, true);
               totalPlayers++;
            }
            else if(!p1.havePlayerAttributesBeenSet() && p1.isActionOn(Action.JUMPING))
            {
                upgradePlayer(e1, totalPlayers, true);
                totalPlayers++;
            }
            else if(!p2.havePlayerAttributesBeenSet() && p2.isActionOn(Action.JUMPING))
            {
                upgradePlayer(e2, totalPlayers, true);
                totalPlayers++;
            }
            else if(!p3.havePlayerAttributesBeenSet() && p3.isActionOn(Action.JUMPING))
            {
                upgradePlayer(e3, totalPlayers, true);
                totalPlayers++;
            }
        }

        //Remove player stuffs
        if(totalPlayers > 0)
        {
            if     (p0.havePlayerAttributesBeenSet() && p0.isActionOn(Action.ABILITY_3))
            {
                removePlayer(p0.playerNum, e0);
                totalPlayers--;
            }
            else if(p1.havePlayerAttributesBeenSet() && p1.isActionOn(Action.ABILITY_3))
            {
                removePlayer(p1.playerNum, e1);
                totalPlayers--;
            }
            else if(p2.havePlayerAttributesBeenSet() && p2.isActionOn(Action.ABILITY_3))
            {
                removePlayer(p2.playerNum, e2);
                totalPlayers--;
            }
            else if(p3.havePlayerAttributesBeenSet() && p3.isActionOn(Action.ABILITY_3))
            {
                removePlayer(p3.playerNum, e3);
                totalPlayers--;
            }
        }

    }


    //resets the player that wanted to leave and cascades the other players down (remakes their other components!)
    void removePlayer(int playerNum, Entity playerEntity)
    {
        EntityFactory.resetPlayerToControllerOnly(world, b2world, playerEntity);

        removeFinalPortrait();

        Player p0 = e0.getComponent(Player.class);
        Player p1 = e1.getComponent(Player.class);
        Player p2 = e2.getComponent(Player.class);
        Player p3 = e3.getComponent(Player.class);

        int origPlayerNum;
        if (p0.havePlayerAttributesBeenSet() && (origPlayerNum = p0.playerNum) > playerNum)
        {
            EntityFactory.resetPlayerToControllerOnly(world, b2world, e0);
            upgradePlayer(e0, origPlayerNum - 1, false);
        }
        if (p1.havePlayerAttributesBeenSet() && (origPlayerNum = p1.playerNum) > playerNum)
        {
            EntityFactory.resetPlayerToControllerOnly(world, b2world, e1);
            upgradePlayer(e1, origPlayerNum - 1, false);
        }
        if (p2.havePlayerAttributesBeenSet() && (origPlayerNum = p2.playerNum) > playerNum)
        {
            EntityFactory.resetPlayerToControllerOnly(world, b2world, e2);
            upgradePlayer(e2, origPlayerNum - 1, false);
        }
        if (p3.havePlayerAttributesBeenSet() && (origPlayerNum = p3.playerNum) > playerNum)
        {
            EntityFactory.resetPlayerToControllerOnly(world, b2world, e3);
            upgradePlayer(e3, origPlayerNum - 1, false);
        }
    }


    void removeFinalPortrait()
    {
        if(totalPlayers == 4)
            world.deleteEntity(por3);
        else if(totalPlayers == 3)
            world.deleteEntity(por2);
        else if(totalPlayers == 2)
            world.deleteEntity(por1);
        else if(totalPlayers == 1)
            world.deleteEntity(por0);

    }

    void upgradePlayer(Entity playerEntity, int playerId, boolean makePortrait)
    {
        switch(playerId)
        {
            case 0:
                EntityFactory.upgradePlayer(world,  0,  0,             true, 0, Constants.P1_UI_POSITION.cpy(), playerEntity);
                if (makePortrait)
                    por0 = EntityFactory.createCharacterPortrait(world, Constants.PIRATE_PORTRAIT_HEALTHY, Constants.P1_UI_POSITION.cpy(), Constants.PORTRAIT_TEAM_RED, 0);

                break;
            case 1:
                EntityFactory.upgradePlayer(world, 1, (float) Math.PI, false, 1, Constants.P2_UI_POSITION.cpy(),  playerEntity);
                if (makePortrait)
                    por1 = EntityFactory.createCharacterPortrait(world, Constants.NINJA_PORTRAIT_HEALTHY, Constants.P2_UI_POSITION.cpy(), Constants.PORTRAIT_TEAM_BLUE, 1);

                break;
            case 2:
                EntityFactory.upgradePlayer(world, 2, (float) Math.PI/2, true, 1, Constants.P3_UI_POSITION.cpy(),  playerEntity);
                if (makePortrait)
                    por2 = EntityFactory.createCharacterPortrait(world, Constants.GANGSTA_PORTRAIT_HEALTHY, Constants.P3_UI_POSITION.cpy(), Constants.PORTRAIT_TEAM_BLUE, 1);

                break;
            case 3:
                EntityFactory.upgradePlayer(world, 3, (float) Math.PI *3/2, false, 0, Constants.P4_UI_POSITION.cpy(),  playerEntity);
                if (makePortrait)
                    por3 = EntityFactory.createCharacterPortrait(world, Constants.PHARAOH_PORTRAIT_HEALTHY, Constants.P4_UI_POSITION.cpy(), Constants.PORTRAIT_TEAM_RED, 0);

                break;
        }
    }




    private void beginGame()
    {
        camera.rotate(rotationSpeed += .05f);
        logo.rotate(-rotationSpeed);
        camera.zoom -= (zoomIncreasePerFrame += Constants.CAMERA_TRANSITION_ZOOM_ACCEL);


        Entity[] players = null;
        if(totalPlayers == 2)
        {
           players = new Entity[2];
           players[0] = e0;
           players[1] = e1;
        }
        else if(totalPlayers == 4)
        {
            players = new Entity[4];
            players[0] = e0;
            players[1] = e1;
            players[2] = e2;
            players[3] = e3;
        }

        if ((logoAlpha -= .015f) < 0)
            logoAlpha = 0;

        logo.setColor(1, 1, 1, logoAlpha);

        if (camera.zoom <= 0)
        {
            game.setScreen(new GameScreen(game,players));
        }
    }

    private void draw()
    {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.disableBlending();
        batch.draw(background, BACKGROUND_X, BACKGROUND_Y);
        batch.enableBlending();

        arenaRing.draw(batch);
        logo.draw(batch);

        batch.end();
    }

    @Override
    public void dispose()
    {
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

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
//        if (keycode == Input.Keys.ENTER && (totalPlayers == 2 || totalPlayers == 4))
//        {
//            beginGame();
//            return true;
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
