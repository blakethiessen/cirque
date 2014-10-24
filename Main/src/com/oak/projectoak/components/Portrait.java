package com.oak.projectoak.components;

import com.artemis.Component;

public class Portrait extends Component
{
    public PortraitPair[] portraitPairs;
    public String deathPortrait;

    public Portrait(String[] portraitStates, String deathPortraitTextureName)
    {
        portraitPairs = new PortraitPair[portraitStates.length];
        for (int i = 0; i < portraitStates.length; i++)
        {
            portraitPairs[i] = new PortraitPair(portraitStates[i]);
        }

        deathPortrait = deathPortraitTextureName;
    }

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
