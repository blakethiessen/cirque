package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.oak.projectoak.Constants;

/*
    The Player component is attached to
    entities that are controlled by players.
 */

public class Player extends Component
{
    public int playerNum;

    public Player()
    {
        // TODO: Deal with player removals.
        playerNum = ++Constants.curPlayersActive;
    }
}
