package com.blakeandshahan.cirque.gamemodemanagers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.blakeandshahan.cirque.AssetLoader;
import com.blakeandshahan.cirque.Constants;
import com.blakeandshahan.cirque.components.TextRender;
import com.blakeandshahan.cirque.entity.EntityFactory;

public class DeathMatchManager extends GameModeManager
{
    private final int numOfTeams;
    private final int maxDeaths;

    private int[] livesLeft;
    private TextRender[] livesLeftText;

    private Entity winText;

    public DeathMatchManager(int numOfTeams, int maxDeaths)
    {
        this.numOfTeams = numOfTeams;
        this.maxDeaths = maxDeaths;
        livesLeft = new int[numOfTeams];
        livesLeftText = new TextRender[numOfTeams];

        // Setting winner to 0 at first in order to make the game seem ended.
        winner = 0;
    }

    @Override
    public void resetGame()
    {
        super.resetGame();

        if (winText != null)
            EntityFactory.engine.removeEntity(winText);

        if (numOfTeams == 2)
        {
            String maxDeathsString = String.valueOf(maxDeaths);

            livesLeft[0] = maxDeaths;
            livesLeft[1] = maxDeaths;

            if (livesLeftText[0] == null)
            {
                livesLeftText[0] = EntityFactory.createText(maxDeathsString,
                        new Vector2(Constants.UI_PADDING + 45, Gdx.graphics.getHeight() / 2 + 45),
                        new Color(1, 127f/255f, 127f/255f, 1), 1)
                        .getComponent(TextRender.class);
                livesLeftText[1] = EntityFactory.createText(maxDeathsString, new Vector2(
                                Gdx.graphics.getWidth() - Constants.UI_PADDING - 45, Gdx.graphics.getHeight() / 2 + 45),
                        new Color(110f/255f, 200f/255f, 230f/255f, 1), 1)
                        .getComponent(TextRender.class);
            }
            else
            {
                for (int i = 0; i < livesLeftText.length; i++)
                    livesLeftText[i].text = String.valueOf(livesLeft[i]);
            }
        }
        else
        {
            Gdx.app.log("DeathMatch Setup Error", "Not able to setup for " + numOfTeams + " teams.");
        }
    }

    public void addKillStatistic(int teamNum)
    {
        if (--livesLeft[teamNum] == 0)
        {
            if (teamNum == 0)
                winner = 2;
            else
                winner = 1;

            winText = EntityFactory.createText("Team " + winner + " wins!",
                    new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2),
                    Color.WHITE, Constants.WIN_TEXT_SCALE);

            AssetLoader.fadeMusic();
        }

        livesLeftText[teamNum].text = String.valueOf(livesLeft[teamNum]);
    }
}
