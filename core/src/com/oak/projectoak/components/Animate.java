package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.oak.projectoak.AssetLoader;

/*
    The Animate component is for any entity
    that has animations it can run.
 */

public class Animate extends Component
{
    private Animation animation;

    public int animationArrayLocation;

    public float stateTime = 0;
    public boolean playWithNoInterrupts;

    public boolean playOnce;

    public Animate(String initialAnimation, boolean playOnce)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        this.playOnce = playOnce;
        animationArrayLocation = 0;
    }

    public Animate(String initialAnimation, int animationArrayLocation)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        playWithNoInterrupts = false;
        playOnce = false;
        this.animationArrayLocation = animationArrayLocation;
    }

    public Animate(String initialAnimation)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        playWithNoInterrupts = false;
        playOnce = false;
        animationArrayLocation = 0;
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
}
