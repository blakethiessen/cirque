package com.oak.projectoak.physics.userdata;

import com.artemis.Entity;

public class EntityUD implements UserData
{
    public Entity entity;

    public EntityUD(Entity entity)
    {
        this.entity = entity;
    }
}
