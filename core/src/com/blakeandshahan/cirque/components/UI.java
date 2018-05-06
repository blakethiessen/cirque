package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class UI implements Component, Pool.Poolable
{
    public UI init()
    {
        return this;
    }

    @Override
    public void reset() {}
}
