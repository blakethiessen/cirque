package com.oak.projectoak.systems.ability;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
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

    public PillarSystem()
    {
        super(Aspect.getAspectForAll(Pillar.class));
    }

    @Override
    protected void process(Entity e)
    {
        Pillar pillar = pm.get(e);
        TrapPhysics trapPhysics = tpm.get(e);
        Render render = rm.get(e);

        if (pillar.numOfPillarSegments > pillar.prevNumOfPillarSegments)
        {
            addPillarSegments(pillar, trapPhysics, render);
        }
        else if (pillar.numOfPillarSegments < pillar.prevNumOfPillarSegments)
        {
            removePillarSegments(pillar, trapPhysics, render);
        }
    }

    private void addPillarSegments(Pillar pillar, TrapPhysics trapPhysics, Render render)
    {
        PolygonShape polygonShape = (PolygonShape)(trapPhysics.fixture.getShape());
        polygonShape.set(Constants.adjustFixtureTransform(
                Box2DDefs.getPillarVertices(pillar.numOfPillarSegments),
                trapPhysics.localPosition,
                trapPhysics.initialRotation));

        pillar.prevNumOfPillarSegments = pillar.numOfPillarSegments;
    }

    private void removePillarSegments(Pillar pillar, TrapPhysics trapPhysics, Render render)
    {

        pillar.prevNumOfPillarSegments = pillar.numOfPillarSegments;
    }
}
