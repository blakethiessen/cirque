package com.blakeandshahan.cirque.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.Mapper;
import com.blakeandshahan.cirque.components.Animate;
import com.blakeandshahan.cirque.components.Render;

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
                    (TextureRegion)animate.getAnimation().getKeyFrame(animate.stateTime, true));

            animate.stateTime += Gdx.graphics.getDeltaTime();
            animate.resetAnimationIfDone();
        }
    }
}
