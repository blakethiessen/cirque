package com.oak.projectoak.systems.render;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchStarter extends VoidEntitySystem
{
    private final SpriteBatch spriteBatch;

    public SpriteBatchStarter(SpriteBatch spriteBatch)
    {
        this.spriteBatch = spriteBatch;
    }

    @Override
    protected void processSystem()
    {
        spriteBatch.begin();
    }
}
