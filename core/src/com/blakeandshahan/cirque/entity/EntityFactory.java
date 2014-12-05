package com.blakeandshahan.cirque.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.blakeandshahan.cirque.AbilityType;
import com.blakeandshahan.cirque.AssetLoader;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.*;
import com.blakeandshahan.cirque.components.Render.Layer;
import com.blakeandshahan.cirque.components.physics.ArenaTransform;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;
import com.blakeandshahan.cirque.components.physics.Physics;
import com.blakeandshahan.cirque.components.physics.TrapPhysics;
import com.blakeandshahan.cirque.physics.Box2DDefs;
import com.blakeandshahan.cirque.physics.PhysicsFactory;
import com.blakeandshahan.cirque.physics.userdata.LethalUD;
import com.blakeandshahan.cirque.physics.userdata.PillarUD;
import com.blakeandshahan.cirque.systems.ability.AbilityDestructionSystem;

/*
    The EntityFactory is where all entities
    are composed of their components.
 */

public class EntityFactory
{
    // Must be set at beginning of game!!
    public static PooledEngine engine;

    // Creates a slot for controller input. Extends into a Player after pressing "Start" with a controller.
    public static Entity createController(int playerNum)
    {
        Entity e = engine.createEntity();
        e.add(engine.createComponent(PlayerController.class).init(playerNum));

        engine.addEntity(e);

        return e;
    }

    // Generates a player entity. Requires a controllerEntity created via above. Note if
    // parameter chosenAbilityTypes is null, the AbilityUser component is not added to the player.
    public static void createPlayerFromController(Entity controllerEntity, float radialPosition,
            boolean onOutsideEdge, int teamNum, Vector2 uiPosition, AbilityType[] chosenAbilityTypes,
            AbilityDestructionSystem abilityDestructionSystem)
    {
        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        controllerEntity.add(engine.createComponent(DynamicPhysics.class)
                .init(PhysicsFactory.createRunnerBody(controllerEntity), twoDPosition));
        controllerEntity.add(engine.createComponent(Render.class).init(Layer.PLAYERS, twoDPosition, false));

        Platformer platformer = engine.createComponent(Platformer.class).init(
                Constants.PLAYER_LAT_ACCEL, Constants.PLAYER_LAT_MAX_VEL,
                onOutsideEdge ? Constants.OUTER_PLAYER_JUMP_ACCEL : Constants.INNER_PLAYER_JUMP_ACCEL);
        controllerEntity.add(platformer);
        abilityDestructionSystem.addFootContactUser(platformer, onOutsideEdge);

        boolean uiOnRightSide = uiPosition.x > Gdx.graphics.getWidth() / 2;

        if (uiOnRightSide)
            uiPosition.x -= Constants.PORTRAIT_WIDTH;

        final int playerNum = Mapper.playerController.get(controllerEntity).controllerNum;

        Entity characterPortrait;
        // TODO: This should be cleaner.
        if (playerNum == 0)
        {
            controllerEntity.add(engine.createComponent(Animate.class).init(Constants.PIRATE_IDLE));
            controllerEntity.add(
                    engine.createComponent(PlayerAnimation.class).init(PlayerAnimation.AnimationSet.PIRATE));
            characterPortrait =
                    createCharacterPortrait(new String[]
                                    {
                                            Constants.PIRATE_PORTRAIT_HEALTHY,
                                            Constants.PIRATE_PORTRAIT_BRUISED,
                                            Constants.PIRATE_PORTRAIT_NEAR_DEAD
                                    },
                            Constants.PIRATE_PORTRAIT_DEAD, uiPosition, Constants.PORTRAIT_TEAM_RED);
        }
        else if (playerNum == 1)
        {
            controllerEntity.add(engine.createComponent(Animate.class).init(Constants.NINJA_IDLE));
            controllerEntity.add(
                    engine.createComponent(PlayerAnimation.class).init(PlayerAnimation.AnimationSet.NINJA));
            characterPortrait =
                    createCharacterPortrait(new String[]
                                    {
                                            Constants.NINJA_PORTRAIT_HEALTHY,
                                            Constants.NINJA_PORTRAIT_BRUISED,
                                            Constants.NINJA_PORTRAIT_NEAR_DEAD
                                    },
                            Constants.NINJA_PORTRAIT_DEAD, uiPosition, Constants.PORTRAIT_TEAM_BLUE);
        }
        else if (playerNum == 2)
        {
            controllerEntity.add(engine.createComponent(Animate.class).init(Constants.PHARAOH_IDLE));
            controllerEntity.add(
                    engine.createComponent(PlayerAnimation.class).init(PlayerAnimation.AnimationSet.PHARAOH));
            characterPortrait =
                    createCharacterPortrait(new String[]
                                    {
                                            Constants.PHARAOH_PORTRAIT_HEALTHY,
                                            Constants.PHARAOH_PORTRAIT_BRUISED,
                                            Constants.PHARAOH_PORTRAIT_NEAR_DEAD
                                    },
                            Constants.PHARAOH_PORTRAIT_DEAD, uiPosition, Constants.PORTRAIT_TEAM_RED);
        }
        else
        {
            controllerEntity.add(engine.createComponent(Animate.class).init(Constants.GANGSTA_IDLE));
            controllerEntity.add(
                    engine.createComponent(PlayerAnimation.class).init(PlayerAnimation.AnimationSet.GANGSTA));
            characterPortrait =
                    createCharacterPortrait(new String[]
                                    {
                                            Constants.GANGSTA_PORTRAIT_HEALTHY,
                                            Constants.GANGSTA_PORTRAIT_BRUISED,
                                            Constants.GANGSTA_PORTRAIT_NEAR_DEAD
                                    },
                            Constants.GANGSTA_PORTRAIT_DEAD, uiPosition, Constants.PORTRAIT_TEAM_BLUE);
        }

        controllerEntity.add(engine.createComponent(Player.class).init(teamNum, characterPortrait));
        controllerEntity.add(engine.createComponent(ArenaTransform.class).init(radialPosition, onOutsideEdge));
        
        if (chosenAbilityTypes != null)
            addAbilities(controllerEntity, chosenAbilityTypes);
    }

    public static void addAbilities(Entity playerEntity, AbilityType[] chosenAbilityTypes)
    {
        Sprite portraitSprite = Mapper.player.get(playerEntity).portraitRender.sprites[0];
        Vector2 uiPosition = new Vector2(portraitSprite.getX(), portraitSprite.getY());

        // Add abilities
        AbilityCreation[] abilityCreationComponents = new AbilityCreation[chosenAbilityTypes.length];

        // If the position is on the right side, move our uiPosition origin to the left to compensate.
        if (uiPosition.x > Gdx.graphics.getWidth() / 2)
        {
            uiPosition.x -= Constants.ENERGY_METER_WIDTH *
                    abilityCreationComponents.length + Constants.PORTRAIT_ENERGY_METER_PADDING;
        }
        else
            uiPosition.x += Constants.PORTRAIT_WIDTH + Constants.PORTRAIT_ENERGY_METER_PADDING;

        uiPosition.y += Constants.PORTRAIT_WIDTH / 4;

        for (int i = 0; i < chosenAbilityTypes.length; i++)
        {
            abilityCreationComponents[i] = createAbilityCreator(chosenAbilityTypes[i], uiPosition);
            uiPosition.x += Constants.ENERGY_METER_WIDTH;
        }

        playerEntity.add(engine.createComponent(AbilityUser.class).init(abilityCreationComponents));
    }

    private static Entity createCharacterPortrait(String[] portraitStates,
                                                  String deathPortrait, Vector2 screenPosition, String teamColor)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(UI.class).init());
        e.add(engine.createComponent(Portrait.class).init(portraitStates, deathPortrait));

        String[] imgArray = new String[]{teamColor, portraitStates[0]};
        e.add(engine.createComponent(Render.class).init(imgArray, Layer.UI, screenPosition, true));

        engine.addEntity(e);

        return e;
    }

    // Returns abilityCreation component because that's all the player should need.
    private static AbilityCreation createAbilityCreator(AbilityType abilityType, Vector2 screenPosition)
    {
        String[] bubbleTextures = new String[4];
        bubbleTextures[1] = Constants.UI_ENERGY_METER_FILL;
        // 3rd layer is for animation.
        bubbleTextures[3] = null;

        switch (abilityType)
        {
            case STAKE:
                bubbleTextures[0] = Constants.UI_SPIKE_METER;
                bubbleTextures[2] = Constants.UI_ENERGY_METER_3_LEVEL;
                break;
            case PILLAR:
                bubbleTextures[0] = Constants.UI_PILLAR_METER;
                bubbleTextures[2] = Constants.UI_ENERGY_METER_3_LEVEL;
                break;
            case LIGHTNING_BOLT:
                bubbleTextures[0] = Constants.UI_LIGHTNING_METER;
                bubbleTextures[2] = Constants.UI_ENERGY_METER_2_LEVEL;
                break;
            default:
                Gdx.app.error(
                        "Unknown Ability UI", "UI for an unknown ability: " + abilityType + " could not be created.");
        }

        Entity e = engine.createEntity();

        AbilityCreation abilityCreationComponent = engine.createComponent(AbilityCreation.class).init(abilityType);
        e.add(abilityCreationComponent);
        e.add(engine.createComponent(UI.class).init());
        final Render render = engine.createComponent(Render.class).init(bubbleTextures, Layer.UI, screenPosition, true);

        final Sprite energyFillSprite = render.sprites[1];
        float energyOffset = (render.sprites[2].getHeight() - energyFillSprite.getHeight()) / 2;

        render.sprites[0].setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);
        energyFillSprite.setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);

        e.add(render);

        final Animate animate = engine.createComponent(Animate.class).init(Constants.UI_ENERGY_READY, 3);
        animate.playOnce = true;
        e.add(animate);

        engine.addEntity(e);

        return abilityCreationComponent;
    }

    public static Entity createTrapRing(Vector2 position, float initialSpin)
    {
        Entity e = engine.createEntity();

        DynamicPhysics dynamicPhysics =
                engine.createComponent(DynamicPhysics.class).init(PhysicsFactory.createTrapRingBody(), position);

        ArenaRotation arenaRotation = engine.createComponent(ArenaRotation.class).init();
        arenaRotation.rotationVelocity = initialSpin;

        dynamicPhysics.body.setAngularVelocity(initialSpin);

        e.add(arenaRotation);
        e.add(dynamicPhysics);
        e.add(engine.createComponent(RenderOffset.class).init(new Vector2(Constants.ARENA_OUTER_RADIUS +
                Constants.ARENA_EDGE_WIDTH - .1f, Constants.ARENA_OUTER_RADIUS + Constants.ARENA_EDGE_WIDTH - .1f)));

        e.add(engine.createComponent(Render.class).init(Constants.OUTER_RING, Layer.ARENA, position, true));

        engine.addEntity(e);

        return e;
    }

    public static Entity createArenaCircle(Vector2 position)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(Physics.class).init(PhysicsFactory.createArenaCircleBody(), position));

        engine.addEntity(e);

        return e;
    }

    public static Entity createStake(Body trapRingBody, float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = engine.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        final TrapPhysics trapPhysics = engine.createComponent(TrapPhysics.class).init(Box2DDefs.STAKE_FIXTURE_DEF,
                Box2DDefs.getSpikeVertices(), trapRingBody, twoDPosition, radialPosition, onOutsideEdge);
        trapPhysics.fixture.setUserData(new LethalUD(e));
        e.add(trapPhysics);

        e.add(engine.createComponent(Render.class)
                .init(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
        e.add(engine.createComponent(Animate.class).init(Constants.SPIKE, true));
        e.add(engine.createComponent(Ability.class).init(owner));

        engine.addEntity(e);

        AssetLoader.playSound("spike");

        return e;
    }

    public static Entity createPillar(Body trapRingBody,
                                      float radialPosition, boolean onOutsideEdge)
    {
        Entity e = null;

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        boolean existingPillarFound = false;

        for (Fixture fixture : trapRingBody.getFixtureList())
        {
            final Object userData = fixture.getUserData();

            if (userData instanceof PillarUD)
            {
                Entity pillarEntity = ((PillarUD) userData).entity;

                final TrapPhysics trapPhysics = Mapper.trapPhysics.get(pillarEntity);
                // TODO: NullPointerException
                final float curTrapPos = trapPhysics.initialRadialPosition + trapRingBody.getAngle();
                if (curTrapPos - Constants.PILLAR_STACK_RANGE < radialPosition &&
                    curTrapPos + Constants.PILLAR_STACK_RANGE > radialPosition)
                {
                    Pillar pillar = Mapper.pillar.get(pillarEntity);

                    pillar.numOfPillarSegments++;

                    existingPillarFound = true;
                    e = pillarEntity;

                    break;
                }
            }
        }

        if (!existingPillarFound)
        {
            e = engine.createEntity();
            e.add(engine.createComponent(Pillar.class).init());

            final TrapPhysics trapPhysics = engine.createComponent(TrapPhysics.class).init(Box2DDefs.PILLAR_FIXTURE_DEF,
                    Box2DDefs.getPillarVertices(1),
                    trapRingBody, twoDPosition, radialPosition, onOutsideEdge, 0);
            trapPhysics.fixture.setUserData(new PillarUD(e));
            e.add(trapPhysics);

            e.add(engine.createComponent(Render.class)
                    .init(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
            e.add(engine.createComponent(Animate.class).init(Constants.PILLAR, true));

            engine.addEntity(e);
        }

        AssetLoader.playSound("pillar");

        return e;
    }

    public static Entity createLightningBolt(final float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = engine.createEntity();

        Vector2 twoDPosition;
        if (onOutsideEdge)
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(radialPosition, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_HEIGHT + Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);
        else
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(
                    radialPosition, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);

        DynamicPhysics dynamicPhysics = engine.createComponent(DynamicPhysics.class)
                .init(PhysicsFactory.createLightningBoltBody(e), twoDPosition);
        final Body body = dynamicPhysics.body;
        final Vector2 position = body.getPosition();
        if (onOutsideEdge)
        {
            Vector2 twoDVelocityPosition = Constants.ConvertRadialTo2DPositionWithHeight(
                    radialPosition, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_HEIGHT + Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);

            body.setLinearVelocity(twoDVelocityPosition.cpy().sub(Constants.ARENA_CENTER)
                            .scl(Constants.LIGHTNING_BOLT_OUTSIDE_SPEED_SCALE_FACTOR));

            body.setTransform(position.x, position.y, (float)(radialPosition + Math.PI / 2));

            final Vector2 oppositeSidePos = Constants.ConvertRadialTo2DPositionWithHeight(
                    (float) (radialPosition + Math.PI + Constants.OUTER_LIGHTNING_ROTATION_OFFSET), onOutsideEdge,
                    Constants.LIGHTNING_WRAP_AROUND_SPAWN_DISTANCE);

            Timer.schedule(new Timer.Task()
            {
                @Override
                public void run()
                {
                    body.setTransform(oppositeSidePos.x, oppositeSidePos.y, (float)(radialPosition + Math.PI / 2));
                }
            }, Constants.LIGHTNING_TIME_UNTIL_WRAP_AROUND);
        }
        else
        {
            Vector2 twoDVelocityPosition = Constants.ConvertRadialTo2DPositionWithHeight(
                    radialPosition + Constants.LIGHTNING_BOLT_ROTATION_OFFSET, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);

            body.setLinearVelocity(Constants.ARENA_CENTER.cpy()
                    .sub(twoDVelocityPosition).scl(Constants.LIGHTNING_BOLT_INSIDE_SPEED_SCALE_FACTOR));
            body.setTransform(position.x, position.y,
                    (float)(radialPosition + Math.PI / 2 + Constants.LIGHTNING_BOLT_ROTATION_OFFSET));
        }

        e.add(dynamicPhysics);

        e.add(engine.createComponent(Render.class)
                .init(Constants.LIGHTNING_BOLT, Layer.ABILITIES, Vector2.Zero, false));
        e.add(engine.createComponent(Animate.class).init(Constants.LIGHTNING_BOLT));
        e.add(engine.createComponent(Ability.class).init(owner));

        engine.addEntity(e);

        AssetLoader.playSound("lightning");

        return e;
    }

    //create text with Black color
    public static Entity createText(String text, Vector2 position)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(TextRender.class).init(text, position));

        engine.addEntity(e);

        return e;
    }

    //create colored text with 'color' as the parameter
    public static Entity createText(String text, Vector2 position, Color color, float scale)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(TextRender.class).init(text, position, color, scale));

        engine.addEntity(e);

        return e;
    }

    //Create aligned text
    public static Entity createText(String text, Vector2 position, float scale,
                                    Color color, float alignmentSize, BitmapFont.HAlignment alignment)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(TextRender.class).init(text, position, color, scale, alignmentSize, alignment));

        engine.addEntity(e);

        return e;
    }

    public static Entity createStaticImage(String imagePath, Vector2 position)
    {
        Entity e = engine.createEntity();

        e.add(engine.createComponent(Render.class).init(imagePath, Layer.UI, position, true));

        engine.addEntity(e);

        return e;
    }
}