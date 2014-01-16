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
    public Animation animation;
    public float stateTime = 0;

    public Animate(String initialAnimation)
    {
        animation = AssetLoader.getAnimation(initialAnimation);
    }
}
