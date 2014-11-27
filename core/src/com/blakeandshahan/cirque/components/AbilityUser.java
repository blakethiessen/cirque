package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;

public class AbilityUser extends Component
{
    public AbilityCreation[] abilities;

    public AbilityUser(AbilityCreation[] abilityCreationComponents)
    {
        this.abilities = abilityCreationComponents;
    }
}
