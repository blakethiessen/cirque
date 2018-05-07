package com.blakeandshahan.cirque;

import com.badlogic.gdx.Game;
import com.blakeandshahan.cirque.screens.GameScreen;
import com.blakeandshahan.cirque.screens.TitleScreen;

/*
    This is where the game is run from, independent of
    whether it's on mobile, desktop, etc.
 */

public class Cirque extends Game
{
    @Override
    public void create()
    {
        AssetLoader.setMusic("godfighter_soundtrack.mp3");
        setScreen(new GameScreen(this));
    }
}
