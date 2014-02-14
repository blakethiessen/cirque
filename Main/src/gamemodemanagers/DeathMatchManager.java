package gamemodemanagers;

public class DeathMatchManager extends GameModeManager
{
    private int[] teamDeaths;
    private final int maxDeaths;

    public DeathMatchManager(int numOfTeams, int maxDeaths)
    {
        this.maxDeaths = maxDeaths;
        teamDeaths = new int[numOfTeams];
    }

    public void addKillStatistic(int teamNum)
    {
        if (++teamDeaths[teamNum] >= maxDeaths)
        {
            winner = teamNum;
        }
    }
}
