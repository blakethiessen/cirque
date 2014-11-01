package com.oak.projectoak.systems.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchStarter extends EntitySystem
{
    private final SpriteBatch spriteBatch;

    public SpriteBatchStarter(SpriteBatch spriteBatch)
    {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void update(float deltaTime)
    {
        spriteBatch.begin();
    }
}
