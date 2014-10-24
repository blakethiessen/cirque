package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Pillar;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.physics.TrapPhysics;
import com.oak.projectoak.physics.Box2DDefs;

/*
    Manages the updating of pillar segment additions and removals.
 */
public class PillarSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Pillar> pm;
    @Mapper ComponentMapper<TrapPhysics> tpm;
    @Mapper ComponentMapper<Render> rm;

    private final AbilityDestructionSystem abilityDestructionSystem;

    public PillarSystem(AbilityDestructionSystem abilityDestructionSystem)
    {
        super(Aspect.getAspectForAll(Pillar.class));
        this.abilityDestructionSystem = abilityDestructionSystem;
    }

    @Override
    protected void process(Entity e)
    {
        Pillar pillar = pm.get(e);

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
        TrapPhysics trapPhysics = tpm.get(e);

        changeFixtureSize(pillar, trapPhysics);

        Render render = rm.get(e);
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
            TrapPhysics trapPhysics = tpm.get(e);
            changeFixtureSize(pillar, trapPhysics);

            Render render = rm.get(e);
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
