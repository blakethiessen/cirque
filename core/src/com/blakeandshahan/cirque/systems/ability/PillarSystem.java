package com.blakeandshahan.cirque.systems.ability;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Pillar;
import com.blakeandshahan.cirque.components.Render;
import com.blakeandshahan.cirque.components.physics.TrapPhysics;
import com.blakeandshahan.cirque.physics.Box2DDefs;

/*
    Manages the updating of pillar segment additions and removals.
 */
public class PillarSystem extends IteratingSystem
{
    private final AbilityDestructionSystem abilityDestructionSystem;

    public PillarSystem(AbilityDestructionSystem abilityDestructionSystem)
    {
        super(Family.all(Pillar.class).get());
        this.abilityDestructionSystem = abilityDestructionSystem;
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        Pillar pillar = Mapper.pillar.get(e);

        long now = System.currentTimeMillis();

        while (pillar.pillarTimeouts.size() > 0 && now >= pillar.pillarTimeouts.peek())
        {
            pillar.numOfPillarSegments--;
            pillar.pillarTimeouts.remove();
        }

        if (pillar.numOfPillarSegments > pillar.prevNumOfPillarSegments)
        {
            addPillarSegments(e, pillar);
        }
        else if (pillar.numOfPillarSegments < pillar.prevNumOfPillarSegments)
        {
            removePillarSegments(e, pillar);
        }
    }

    private void addPillarSegments(Entity e, Pillar pillar)
    {
        TrapPhysics trapPhysics = Mapper.trapPhysics.get(e);

        changeFixtureSize(pillar, trapPhysics);

        Render render = Mapper.render.get(e);
        Sprite curSprite = render.sprites[0];

        // TODO: Repeat the texture.
        curSprite.getTexture().setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
        curSprite.setSize(curSprite.getWidth(), curSprite.getRegionHeight() * pillar.numOfPillarSegments);

        pillar.pillarTimeouts.add(System.currentTimeMillis() + Constants.PILLAR_LIFETIME_MS);
        pillar.prevNumOfPillarSegments = pillar.numOfPillarSegments;
    }

    private void removePillarSegments(Entity e, Pillar pillar)
    {
        if (pillar.numOfPillarSegments < 1)
            abilityDestructionSystem.destroyEntity(e);
        else
        {
            TrapPhysics trapPhysics = Mapper.trapPhysics.get(e);
            changeFixtureSize(pillar, trapPhysics);

            Render render = Mapper.render.get(e);
            Sprite curSprite = render.sprites[0];

            // TODO: Repeat the texture.
            curSprite.getTexture().setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);
            curSprite.setSize(curSprite.getWidth(), curSprite.getRegionHeight() * pillar.numOfPillarSegments);
        }

        pillar.prevNumOfPillarSegments = pillar.numOfPillarSegments;
    }


    private void changeFixtureSize(Pillar pillar, TrapPhysics trapPhysics)
    {
        PolygonShape polygonShape = (PolygonShape)(trapPhysics.fixture.getShape());
        polygonShape.set(Constants.adjustFixtureTransform(
                Box2DDefs.getPillarVertices(pillar.numOfPillarSegments),
                trapPhysics.localPosition,
                trapPhysics.initialRotation));
    }
}
