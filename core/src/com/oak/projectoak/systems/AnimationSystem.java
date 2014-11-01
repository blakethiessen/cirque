package com.oak.projectoak.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.oak.projectoak.Constants;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Render;

/*
    The AnimationSystem runs animations on all components
    with the Animate and Render components.
 */

public class AnimationSystem extends IteratingSystem
{
    public AnimationSystem()
    {
        super(Family.getFor(Animate.class, Render.class));
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        Animate animate = Mapper.animate.get(e);
        Render render = Mapper.render.get(e);

        // If the animation isn't set, we're rendering static images.
        if (animate != null && render != null && animate.getAnimation() != null)
        {
            Constants.setSpriteTexture(render.sprites[animate.animationArrayLocation],
                    animate.getAnimation().getKeyFrame(animate.stateTime, true));

            animate.stateTime += Gdx.graphics.getDeltaTime();
            animate.resetAnimationIfDone();
        }
    }
}
