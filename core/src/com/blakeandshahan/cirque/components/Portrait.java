package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class Portrait implements Component, Pool.Poolable
{
    public PortraitPair[] portraitPairs;
    public String deathPortrait;

    public Portrait init(String[] portraitStates, String deathPortraitTextureName)
    {
        portraitPairs = new PortraitPair[portraitStates.length];
        for (int i = 0; i < portraitStates.length; i++)
        {
            portraitPairs[i] = new PortraitPair(portraitStates[i]);
        }

        deathPortrait = deathPortraitTextureName;

        return this;
    }

    @Override
    public void reset() {}

    public class PortraitPair
    {
        public String normal;
        public String blink;

        public PortraitPair(String portraitTextureName)
        {
            normal = portraitTextureName;
            blink = portraitTextureName + "_blink";
        }
    }
}
