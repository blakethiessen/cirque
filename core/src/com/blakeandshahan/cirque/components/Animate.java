package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.AssetLoader;

/*
    The Animate component is for any entity
    that has animations it can run.
 */

public class Animate implements Component, Pool.Poolable
{
    private Animation animation;

    public int animationArrayLocation;

    public float stateTime = 0;
    public boolean playWithNoInterrupts;

    public boolean playOnce;

    public Animate init(String initialAnimation, int animationArrayLocation, boolean playOnce)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        this.animationArrayLocation = animationArrayLocation;
        this.playOnce = playOnce;
        playWithNoInterrupts = false;

        return this;
    }

    public Animate init(String initialAnimation, boolean playOnce)
    {
        return init(initialAnimation, 0, playOnce);
    }

    public Animate init(String initialAnimation, int animationArrayLocation)
    {
        return init(initialAnimation, animationArrayLocation, false);
    }

    public Animate init(String initialAnimation)
    {
        return init(initialAnimation, 0, false);
    }

    public void setAnimation(String animationString, boolean playFull)
    {
        if (!playWithNoInterrupts)
        {
            final Animation newAnimation = AssetLoader.getAnimation(animationString);
            if (animation != newAnimation)
            {
                this.animation = newAnimation;
                stateTime = 0;
                playWithNoInterrupts = playFull;
            }
        }
    }

    public void setAnimation(String animationString)
    {
        setAnimation(animationString, false);
    }

    public Animation getAnimation()
    {
        return animation;
    }

    public void setToStaticTexture()
    {
        animation = null;
    }

    public void resetAnimationIfDone()
    {
        if (animation.isAnimationFinished(stateTime))
        {
            playWithNoInterrupts = false;

            if (playOnce)
            {
                setToStaticTexture();
            }
        }
    }

    @Override
    public void reset() {}
}
