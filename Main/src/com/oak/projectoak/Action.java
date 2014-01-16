package com.oak.projectoak;

/*
    A list of all possible actions players can do.
*/

public enum Action
{
    // Player actions
    MOVING_LEFT(0),
    MOVING_RIGHT(1),
    JUMPING(2);

    private final int id;

    private Action(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
