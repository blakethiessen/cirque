package com.oak.projectoak.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
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
    //create text with Black color
    public static Entity createText(World world, String text, Vector2 position)
    {
        Entity e = world.createEntity();

        e.addComponent(new TextRender(text, position));

        e.addToWorld();

        return e;
    }

    //create colored text with 'color' as the parameter
    public static Entity createText(World world, String text, Vector2 position, Color color)
    {
        Entity e = world.createEntity();

        e.addComponent(new TextRender(text, position, color));

        e.addToWorld();

        return e;
    }

    //Create aligned text
    public static Entity createText(World world, String text, Vector2 position, Color color, float alignmentSize, BitmapFont.HAlignment alignment)
    {
        Entity e = world.createEntity();

        e.addComponent(new TextRender(text, position, color, alignmentSize, alignment));

        e.addToWorld();

        return e;
    }


    public static Entity createStaticImage(World world, String imagePath, Vector2 position)
    {
        Entity e = world.createEntity();

        e.addComponent(new Render(imagePath, Layer.UI, position, true));

        e.addToWorld();

        return e;
    }

    public static Entity createBasicController(World world)
    {
        Entity e = world.createEntity();

        e.addComponent(new Player(0, 0, null));

        e.addToWorld();

        return e;
    }

    public static Entity createPlayer(World world, int playerNum, float radialPosition, boolean onOutsideEdge, int teamNum, Vector2 uiPosition, AbilityType[] chosenAbilityTypes)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        e.addComponent(new DynamicPhysics(PhysicsFactory.createRunnerBody(e), twoDPosition));
        e.addComponent(new Platformer(Constants.PLAYER_LAT_ACCEL,
                Constants.PLAYER_LAT_MAX_VEL,
                onOutsideEdge ? Constants.OUTER_PLAYER_JUMP_ACCEL : Constants.INNER_PLAYER_JUMP_ACCEL));
        e.addComponent(new Render(Layer.PLAYERS, twoDPosition, false));

        boolean uiOnRightSide = uiPosition.equals(Constants.P2_UI_POSITION) || uiPosition.equals(Constants.P4_UI_POSITION);

        if (uiOnRightSide)
            uiPosition.x -= Constants.PORTRAIT_WIDTH;

        if (playerNum == 0)
        {
            e.addComponent(new Animate(Constants.PIRATE_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.PIRATE));
            createCharacterPortrait(world, Constants.PIRATE_PORTRAIT_HEALTHY, uiPosition, Constants.PORTRAIT_TEAM_RED);
        }
        else if (playerNum == 1)
        {
            e.addComponent(new Animate(Constants.NINJA_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.NINJA));
            createCharacterPortrait(world, Constants.NINJA_PORTRAIT_HEALTHY, uiPosition, Constants.PORTRAIT_TEAM_BLUE);
        }
        else if (playerNum == 2)
        {
            e.addComponent(new Animate(Constants.GANGSTA_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.GANGSTA));
            createCharacterPortrait(world, Constants.GANGSTA_PORTRAIT_HEALTHY, uiPosition, Constants.PORTRAIT_TEAM_BLUE);
        }
        else
        {
            e.addComponent(new Animate(Constants.PHARAOH_IDLE));
            e.addComponent(new PlayerAnimation(PlayerAnimation.AnimationSet.PHARAOH));
            createCharacterPortrait(world, Constants.PHARAOH_PORTRAIT_HEALTHY, uiPosition, Constants.PORTRAIT_TEAM_RED);
        }

        e.addComponent(new ArenaTransform(radialPosition, onOutsideEdge));

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);

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
            abilityCreationComponents[i] = createAbilityCreator(world, chosenAbilityTypes[i], uiPosition);
            uiPosition.x += Constants.ENERGY_METER_WIDTH;
        }

        e.addComponent(new Player(playerNum, teamNum, abilityCreationComponents));

        e.addToWorld();

        return e;
    }

    private static Entity createCharacterPortrait(World world, String portraitImage, Vector2 screenPosition, String teamColor)
    {
        Entity e = world.createEntity();

        e.addComponent(new UI());

        String[] imgArray = new String[]{teamColor,portraitImage};
        e.addComponent(new Render(imgArray, Layer.UI, screenPosition, true));

        e.addToWorld();

        return e;
    }

    // Returns abilityCreation component because that's all the player should need.
    private static AbilityCreation createAbilityCreator(World world, AbilityType abilityType, Vector2 screenPosition)
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

        Entity e = world.createEntity();

        AbilityCreation abilityCreationComponent = new AbilityCreation(abilityType);
        e.addComponent(abilityCreationComponent);
        e.addComponent(new UI());
        final Render render = new Render(bubbleTextures, Layer.UI, screenPosition, true);

        final Sprite energyFillSprite = render.sprites[1];
        float energyOffset = (render.sprites[2].getHeight() - energyFillSprite.getHeight()) / 2;

        render.sprites[0].setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);
        energyFillSprite.setPosition(energyFillSprite.getX() + energyOffset, energyFillSprite.getY() + energyOffset);

        e.addComponent(render);

        final Animate animate = new Animate(Constants.UI_ENERGY_READY, 3);
        animate.playOnce = true;
        e.addComponent(animate);

        e.addToWorld();

        return abilityCreationComponent;
    }

    public static Entity createTrapRing(World world, Vector2 position, float initialSpin)
    {
        Entity e = world.createEntity();

        DynamicPhysics dynamicPhysics = new DynamicPhysics(PhysicsFactory.createTrapRingBody(), position);

        ArenaRotation arenaRotation = new ArenaRotation();
        arenaRotation.rotationVelocity = initialSpin;

        dynamicPhysics.body.setAngularVelocity(initialSpin);

        e.addComponent(arenaRotation);
        e.addComponent(dynamicPhysics);
        e.addComponent(new RenderOffset(new Vector2(Constants.ARENA_OUTER_RADIUS + Constants.ARENA_EDGE_WIDTH - .1f, Constants.ARENA_OUTER_RADIUS + Constants.ARENA_EDGE_WIDTH - .1f)));

        e.addComponent(new Render(Constants.OUTER_RING, Layer.ARENA, position, true));

        e.addToWorld();

        return e;
    }

    public static Entity createArenaCircle(World world, Vector2 position)
    {
        Entity e = world.createEntity();

        e.addComponent(new Physics(PhysicsFactory.createArenaCircleBody(), position));

        e.addToWorld();

        return e;
    }

    public static Entity createStake(World world, Body trapRingBody, float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        final TrapPhysics trapPhysics = new TrapPhysics(Box2DDefs.STAKE_FIXTURE_DEF, Box2DDefs.getSpikeVertices(),
                trapRingBody, twoDPosition, radialPosition, onOutsideEdge);
        trapPhysics.fixture.setUserData(new LethalUD(e));
        e.addComponent(trapPhysics);

        e.addComponent(new Render(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
        e.addComponent(new Animate(Constants.SPIKE, true));
        e.addComponent(new Ability(owner));

        e.addToWorld();

        AssetLoader.playSound("spike");

        return e;
    }

    public static Entity createPillar(World world, Body trapRingBody,
                                      float radialPosition, boolean onOutsideEdge)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition = Constants.ConvertRadialTo2DPosition(radialPosition, onOutsideEdge);

        int pillarsUnderneath = 0;
        float exactSelectedPillarPosition = 0f;
        for (Fixture fixture : trapRingBody.getFixtureList())
        {
            final Object userData = fixture.getUserData();
            if (userData instanceof PillarUD)
            {
                Entity pillarEntity = ((PillarUD) userData).entity;
                final TrapPhysics trapPhysics = pillarEntity.getComponent(TrapPhysics.class);
                final float curTrapPos = trapPhysics.initialRadialPosition + trapRingBody.getAngle();
                if (trapPhysics.onOutsideEdge == onOutsideEdge)
                {
                    if (pillarsUnderneath > 0)
                    {
                        // If we've found an intersecting pillar before, we need to add to any pillars on top.
                        if (curTrapPos == exactSelectedPillarPosition)
                        {
                            pillarEntity.getComponent(Pillar.class).pillarsStackedOnTop.add(e);
                            pillarsUnderneath++;
                        }
                    }
                    else if (curTrapPos - Constants.PILLAR_STACK_RANGE < radialPosition && curTrapPos + Constants.PILLAR_STACK_RANGE > radialPosition)
                    {
                        pillarEntity.getComponent(Pillar.class).pillarsStackedOnTop.add(e);

                        exactSelectedPillarPosition = curTrapPos;
                        pillarsUnderneath++;
                    }
                }
            }
        }

        float trapHeight = 0;
        if (pillarsUnderneath > 0)
        {
            trapHeight = Constants.PILLAR_HEIGHT * pillarsUnderneath;
            radialPosition = exactSelectedPillarPosition;
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(exactSelectedPillarPosition, onOutsideEdge, trapHeight);
        }

        e.addComponent(new Pillar());

        final TrapPhysics trapPhysics = new TrapPhysics(Box2DDefs.PILLAR_FIXTURE_DEF, Box2DDefs.getPillarVertices(),
                trapRingBody, twoDPosition, radialPosition, onOutsideEdge, trapHeight);
        trapPhysics.fixture.setUserData(new PillarUD(e));
        e.addComponent(trapPhysics);

        e.addComponent(new Render(Layer.ABILITIES, twoDPosition.scl(Constants.METERS_TO_PIXELS), false));
        e.addComponent(new Animate(Constants.PILLAR, true));

        e.addToWorld();

        AssetLoader.playSound("pillar");

        return e;
    }

    public static Entity createLightningBolt(World world, final float radialPosition, boolean onOutsideEdge, Entity owner)
    {
        Entity e = world.createEntity();

        Vector2 twoDPosition;
        if (onOutsideEdge)
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(radialPosition, onOutsideEdge, Constants.LIGHTNING_BOLT_HEIGHT + Constants.LIGHTNING_BOLT_SPAWN_OFFSET);
        else
            twoDPosition = Constants.ConvertRadialTo2DPositionWithHeight(radialPosition, onOutsideEdge, Constants.LIGHTNING_BOLT_SPAWN_OFFSET);

        DynamicPhysics dynamicPhysics = new DynamicPhysics(PhysicsFactory.createLightningBoltBody(e), twoDPosition);
        final Body body = dynamicPhysics.body;
        final Vector2 position = body.getPosition();
        if (onOutsideEdge)
        {
            body.setLinearVelocity(twoDPosition.cpy().sub(Constants.ARENA_CENTER).scl(Constants.LIGHTNING_BOLT_SPEED_SCALE_FACTOR));
            body.setTransform(position.x, position.y, (float)(radialPosition + Math.PI / 2));

            final Vector2 oppositeSidePos = Constants.ConvertRadialTo2DPositionWithHeight(
                    (float) (radialPosition + Math.PI), onOutsideEdge, Constants.LIGHTNING_WRAP_AROUND_SPAWN_DISTANCE);

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
            body.setLinearVelocity(Constants.ARENA_CENTER.cpy().sub(twoDPosition).scl(Constants.LIGHTNING_BOLT_SPEED_SCALE_FACTOR));
            body.setTransform(position.x, position.y, (float)(radialPosition + Math.PI / 2));
        }

        e.addComponent(dynamicPhysics);

        e.addComponent(new Render(Constants.LIGHTNING_BOLT, Layer.ABILITIES, Vector2.Zero, false));
        e.addComponent(new Animate(Constants.LIGHTNING_BOLT));
        e.addComponent(new Ability(owner));

        e.addToWorld();

        AssetLoader.playSound("lightning");

        return e;
    }


    public static Entity createBackground(World world)
    {
        Entity e = world.createEntity();

        e.addComponent(new Render("background", Layer.BACKGROUND, Vector2.Zero, true));

        e.addToWorld();

        return e;
    }

}