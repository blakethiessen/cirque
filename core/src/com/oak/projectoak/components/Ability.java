package com.oak.projectoak.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class Ability extends Component
{
    public Entity owner;

    public Ability(Entity owner)
    {
        this.owner = owner;
    }
}
