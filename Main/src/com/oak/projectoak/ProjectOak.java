package com.oak.projectoak;

import com.badlogic.gdx.Game;
import com.oak.projectoak.screens.GameScreen;

/*
    This is where the game is run from, independent of
    whether it's on mobile, desktop, etc.
 */

public class ProjectOak extends Game
{
    @Override
    public void create()
    {
        setScreen(new GameScreen());
    }
}
