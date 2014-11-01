package com.oak.projectoak.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.oak.projectoak.AbilityType;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.*;
import com.oak.projectoak.components.Render.Layer;
import com.oak.projectoak.components.physics.ArenaTransform;
import com.oak.projectoak.components.physics.DynamicPhysics;
import com.oak.projectoak.components.physics.Physics;
import com.oak.projectoak.components.physics.TrapPhysics;
import com.oak.projectoak.physics.Box2DDefs;
import com.oak.projectoak.physics.PhysicsFactory;
import com.oak.projectoak.physics.userdata.LethalUD;
import com.oak.projectoak.physics.userdata.PillarUD;

/*
    The EntityFactory is where all entities
    are composed of their components.
 */

public class EntityFactory
{
    // Must be set at beginning of game!!
    public static Engine engine;

    //create text with Black color
    public static Entity createText(String text, Vector2 position)
    {
        Entity e = new Entity();

        e.add(new TextRender(text, position));

        engine.addEntity(e);

        return e;
    }

    //create colored text with 'color' as the parameter
    public static Entity createText(String text, Vector2 position, Color color, float scale)
    {
        Entity e = new Entity();

        e.add(new TextRender(text, position, color, scale));

        engine.addEntity(e);

        return e;
    }

    //Create aligned text
    public static Entity createText(String text, Vector2 position, float scale,
                                    Color color, float alignmentSize, BitmapFont.HAlignment alignment)
    {
        Entity e = new Entity();

        e.add(new TextRender(text, position, color, scale, alignmentSize, alignment));

        engine.addEntity(e);

        return e;
    }


    public static Entity createStaticImage(String imagePath, Vector2 position)
    {
        Entity e = new Entity();

        e.add(new Render(imagePath, Layer.UI, position, true));

        engine.addEntity(e);

        return e;
    }

    public static Entity createPlayer(int playerNum, float radialPosition, boolean onOutsideEdge,
                                      int teamNum, Vector2 uiPosition, AbilityType[] chosenAbilityTypes)
    {
        Entity e = new Entity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        e.add(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), twoDPosition));
        e.add(new Platformer(Constants.PLAYER_LAT_ACCEL,
                Constants.PLAYER_LAT_MAX_VEL,
                onOutsideEdge ? Constants.OUTER_PLAYER_JUMP_ACCEL : Constants.INNER_PLAYER_JUMP_ACCEL));
        e.add(new Render(Layer.PLAYERS, twoDPosition, false));

        boolean uiOnRightSide = uiPosition.equals(Constants.P2_UI_POSITION) || uiPosition.equals(Constants.P4_UI_POSITION);

        if (uiOnRightSide)
            uiPosition.x -= Constants.PORTRAIT_WIDTH;

        Entity characterPortrait;
        if (playerNum == 0)
        {
            e.add(new Animate(Constants.PIRATE_IDLE));
            e.add(new PlayerAnimation(PlayerAnimation.AnimationSet.PIRATE));
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
            e.add(new Animate(Constants.NINJA_IDLE));
            e.add(new PlayerAnimation(PlayerAnimation.AnimationSet.NINJA));
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
            e.add(new Animate(Constants.PHARAOH_IDLE));
            e.add(new PlayerAnimation(PlayerAnimation.AnimationSet.PHARAOH));
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
            e.add(new Animate(Constants.GANGSTA_IDLE));
            e.add(new PlayerAnimation(PlayerAnimation.AnimationSet.GANGSTA));
            characterPortrait =
                    createCharacterPortrait(new String[]
                                    {
                                            Constants.GANGSTA_PORTRAIT_HEALTHY,
                                            Constants.GANGSTA_PORTRAIT_BRUISED,
                                            Constants.GANGSTA_PORTRAIT_NEAR_DEAD
                                    },
                            Constants.GANGSTA_PORTRAIT_DEAD, uiPosition, Constants.PORTRAIT_TEAM_BLUE);
        }

        e.add(new ArenaTransform(radialPosition, onOutsideEdge));

        // Add abilities
        AbilityCreation[] abilityCreationComponents = new AbilityCreation[chosenAbilityTypes.length];

        // If the position is on the right side, move our uiPosition origin to the left to compensate.
        if (uiOnRightSide)
            uiPosition.x -= Constants.ENERGY_METER_WIDTH * abilityCreationComponents.length + Constants.PORTRAIT_ENERGY_METER_PADDING;
        else
            uiPosition.x += Constants.PORTRAIT_WIDTH + Constants.PORTRAIT_ENERGY_METER_PADDING;

        uiPosition.y += Constants.PORTRAIT_WIDTH / 4;

        for (int i = 0; i < chosenAbilityTypes.length; i++)
        {
            abilityCreationComponents[i] = createAbilityCreator(chosenAbilityTypes[i], uiPosition);
            uiPosition.x += Constants.ENERGY_METER_WIDTH;
        }

        e.add(new Player(playerNum, teamNum, abilityCreationComponents, characterPortrait));

        engine.addEntity(e);

        return e;
    }

    private static Entity createCharacterPortrait(String[] portraitStates,
                                                  String deathPortrait, Vector2 screenPosition, String teamColor)
    {
        Entity e = new Entity();

        e.add(new UI());
        e.add(new Portrait(portraitStates, deathPortrait));

        String[] imgArray = new String[]{teamColor, portraitStates[0]};
        e.add(new Render(imgArray, Layer.UI, screenPosition, true));

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
                Gdx.app.error("Unknown Ability UI", "UI for an unknown ability: " + abilityType + " could not be created.");
        }

        Entity e = new Entity();

        AbilityCreation abilityCreationComponent = new AbilityCreation(abilityType);
        e.add(abilityCreationComponent);
        e.add(new UI());
        final Render render = new Render(bubbleTextures, Layer.UI, screenPosition, true);

        final Sprite energyFillSprite = render.sprites[1];
        float energyOffset = (render.sprites[2].getHeight() - energyFillSprite.getHeight()) / 2;

        render.sprites[0].setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);
        energyFillSprite.setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);

        e.add(render);

        final Animate animate = new Animate(Constants.UI_ENERGY_READY, 3);
        animate.playOnce = true;
        e.add(animate);

        engine.addEntity(e);

        return abilityCreationComponent;
    }

    public static Entity createTrapRing(Vector2 position, float initialSpin)
    {
        Entity e = new Entity();

        DynamicPhysics dynamicPhysics = new DynamicPhysics(PhysicsFactory.createTrapRingBody(), position);

        ArenaRotation arenaRotation = new ArenaRotation();
        arenaRotation.rotationVelocity = initialSpin;

        dynamicPhysics.body.setAngularVelocity(initialSpin);

        e.add(arenaRotation);
        e.add(dynamicPhysics);
        e.add(new RenderOffset(new Vector2(Constants.ARENA_OUTER_RADIUS + Constants.ARENA_EDGE_WIDTH - .1f, Constants.ARENA_OUTER_RADIUS + Constants.ARENA_EDGE_WIDTH - .1f)));

        e.add(new Render(Constants.OUTER_RING, Layer.ARENA, position, true));

        engine.addEntity(e);

        return e;
    }

    public static Entity createArenaCircle(Vector2 position)
    {
        Entity e = new Entity();

        e.add(new Physics(PhysicsFactory.createArenaCircleBody(), position));

        engine.addEntity(e);

        return e;
    }

    public static Entity createStake(Body trapRingBody, float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = new Entity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        final TrapPhysics trapPhysics = new TrapPhysics(Box2DDefs.STAKE_FIXTURE_DEF, Box2DDefs.getSpikeVertices(),
                trapRingBody, twoDPosition, radialPosition, onOutsideEdge);
        trapPhysics.fixture.setUserData(new LethalUD(e));
        e.add(trapPhysics);

        e.add(new Render(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
        e.add(new Animate(Constants.SPIKE, true));
        e.add(new Ability(owner));

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

                final TrapPhysics trapPhysics = pillarEntity.getComponent(TrapPhysics.class);
                // TODO: NullPointerException
                final float curTrapPos = trapPhysics.initialRadialPosition + trapRingBody.getAngle();
                if (curTrapPos - Constants.PILLAR_STACK_RANGE < radialPosition &&
                    curTrapPos + Constants.PILLAR_STACK_RANGE > radialPosition)
                {
                    Pillar pillar = pillarEntity.getComponent(Pillar.class);

                    pillar.numOfPillarSegments++;

                    existingPillarFound = true;
                    e = pillarEntity;

                    break;
                }
            }
        }

        if (!existingPillarFound)
        {
            e = new Entity();
            e.add(new Pillar());

            final TrapPhysics trapPhysics = new TrapPhysics(Box2DDefs.PILLAR_FIXTURE_DEF,
                    Box2DDefs.getPillarVertices(1),
                    trapRingBody, twoDPosition, radialPosition, onOutsideEdge, 0);
            trapPhysics.fixture.setUserData(new PillarUD(e));
            e.add(trapPhysics);

            e.add(new Render(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
            e.add(new Animate(Constants.PILLAR, true));

            engine.addEntity(e);
        }

        AssetLoader.playSound("pillar");

        return e;
    }

    public static Entity createLightningBolt(final float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = new Entity();

        Vector2 twoDPosition;
        if (onOutsideEdge)
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(radialPosition, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_HEIGHT + Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);
        else
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(
                    radialPosition, onOutsideEdge,
                    Constants.LIGHTNING_BOLT_SPAWN_HEIGHT_OFFSET);

        DynamicPhysics dynamicPhysics = new DynamicPhysics(PhysicsFactory.createLightningBoltBody(e), twoDPosition);
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

            body.setLinearVelocity(Constants.ARENA_CENTER.cpy().sub(twoDVelocityPosition).scl(Constants.LIGHTNING_BOLT_INSIDE_SPEED_SCALE_FACTOR));
            body.setTransform(position.x, position.y, (float)(radialPosition + Math.PI / 2 + Constants.LIGHTNING_BOLT_ROTATION_OFFSET));
        }

        e.add(dynamicPhysics);

        e.add(new Render(Constants.LIGHTNING_BOLT, Layer.ABILITIES, Vector2.Zero, false));
        e.add(new Animate(Constants.LIGHTNING_BOLT));
        e.add(new Ability(owner));

        engine.addEntity(e);

        AssetLoader.playSound("lightning");

        return e;
    }


    public static Entity createBackground(Engine engine)
    {
        Entity e = new Entity();

        e.add(new Render("background", Layer.BACKGROUND, Vector2.Zero, true));

        engine.addEntity(e);

        return e;
    }

}