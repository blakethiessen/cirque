package com.blakeandshahan.cirque.physics.userdata;

import com.badlogic.ashley.core.Entity;

public class AbilityUD implements UserData
{
    public Entity entity;

    public AbilityUD(Entity entity)
    {
        this.entity = entity;
    }
}
