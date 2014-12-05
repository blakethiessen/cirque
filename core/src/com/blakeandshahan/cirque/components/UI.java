package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class UI extends Component implements Pool.Poolable
{
    public UI init()
    {
        return this;
    }

    @Override
    public void reset() {}
}
