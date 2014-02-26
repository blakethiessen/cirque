package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Render;

/*
    The AnimationSystem runs animations on all components
    with the Animate and Render components.
 */

public class AnimationSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Animate> am;
    @Mapper ComponentMapper<Render> rm;

    public AnimationSystem()
    {
        super(Aspect.getAspectForAll(Animate.class, Render.class));
    }

    @Override
    protected void process(Entity e)
    {
        Animate animate = am.get(e);
        Render render = rm.get(e);

        // If the animation isn't set, we're rendering static images.
        if (animate.getAnimation() != null)
        {
            Constants.setSpriteTexture(render.sprites[0], animate.getAnimation().getKeyFrame(animate.stateTime, true));

            animate.stateTime += Gdx.graphics.getDeltaTime();

            animate.resetAnimationIfDone();
        }
    }
}
