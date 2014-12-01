package com.blakeandshahan.cirque.gamemodemanagers;

public abstract class GameModeManager
{
    protected int winner;

    public GameModeManager()
    {
        winner = -1;
    }

    public boolean isGameOver()
    {
        return winner >= 0;
    }

    public void resetGame()
    {
        winner = -1;
        // Zoom in camera
        // Remove all player & trap entities
        // Display ability select UI
        // Enable ability select control
    }
}
