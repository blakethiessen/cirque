package gamemodemanagers;

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
}
