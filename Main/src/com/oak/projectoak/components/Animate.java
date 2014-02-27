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
    public float stateTime = 0;
    public boolean playFullAnimation;

    public boolean playOnce;

    public Animate(String initialAnimation, boolean playOnce)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        this.playOnce = playOnce;
    }

    public Animate(String initialAnimation)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
        playFullAnimation = false;
        playOnce = false;
    }

    public void setAnimation(String animationString, boolean playFull)
    {
        if (!playFullAnimation)
        {
            final Animation newAnimation = AssetLoader.getAnimation(animationString);
            if (animation != newAnimation)
            {
                this.animation = newAnimation;
                stateTime = 0;
                playFullAnimation = playFull;
            }
        }
    }

    public void setAnimation(String animation)
    {
        setAnimation(animation, false);
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
            playFullAnimation = false;

            if (playOnce)
            {
                setToStaticTexture();
            }
        }
    }
}
