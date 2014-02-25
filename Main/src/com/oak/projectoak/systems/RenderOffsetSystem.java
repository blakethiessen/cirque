package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.oak.projectoak.components.Render;
import com.oak.projectoak.components.RenderOffset;

public class RenderOffsetSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<RenderOffset> rom;
    @Mapper ComponentMapper<Render> rm;

    public RenderOffsetSystem()
    {
        super(Aspect.getAspectForAll(RenderOffset.class, Render.class));
    }

    @Override
    protected void process(Entity e)
    {
        RenderOffset renderOffset = rom.get(e);
        Sprite sprite = rm.get(e).sprite;

        sprite.setPosition(sprite.getX() - renderOffset.pxOffset.x, sprite.getY() - renderOffset.pxOffset.y);
    }
}
