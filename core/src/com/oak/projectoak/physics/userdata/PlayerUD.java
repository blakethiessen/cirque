package com.oak.projectoak.physics.userdata;

import com.badlogic.ashley.core.Entity;

public class PlayerUD implements UserData
{
    public Entity entity;

    public PlayerUD(Entity entity)
    {
        this.entity = entity;
    }
}
