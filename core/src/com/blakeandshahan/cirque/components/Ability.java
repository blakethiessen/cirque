package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class Ability extends Component implements Pool.Poolable
{
    public Entity owner;

    public Ability init(Entity owner)
    {
        this.owner = owner;

        return this;
    }

    @Override
    public void reset() {}
}
