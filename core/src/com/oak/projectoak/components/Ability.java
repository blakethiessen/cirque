package com.oak.projectoak.components;

import com.artemis.Component;
import com.artemis.Entity;

public class Ability extends Component
{
    public Entity owner;

    public Ability(Entity owner)
    {
        this.owner = owner;
    }
}
