package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AbilityUser extends Component implements Pool.Poolable
{
    public AbilityCreation[] abilities;

    public AbilityUser init(AbilityCreation[] abilityCreationComponents)
    {
        this.abilities = abilityCreationComponents;

        return this;
    }

    @Override
    public void reset() {}
}
