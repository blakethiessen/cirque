package com.oak.projectoak.gamemodemanagers;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.entity.EntityFactory;

public class DeathMatchManager extends GameModeManager
{
    private int[] teamDeaths;
    private World world;
    private final int maxDeaths;

    public DeathMatchManager(World world, int numOfTeams, int maxDeaths)
    {
        this.world = world;
        this.maxDeaths = maxDeaths;
        teamDeaths = new int[numOfTeams];
    }

    public void addKillStatistic(int teamNum)
    {
        if (++teamDeaths[teamNum] >= maxDeaths)
        {
            winner = teamNum;
            EntityFactory.createText(world, "Team " + winner + " wins!",
                    new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));
        }
    }
}
