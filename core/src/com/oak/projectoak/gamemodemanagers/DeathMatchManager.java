package com.oak.projectoak.gamemodemanagers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.TextRender;
import com.oak.projectoak.entity.EntityFactory;

public class DeathMatchManager extends GameModeManager
{
    private int[] livesLeft;
    private TextRender[] livesLeftText;
    private Engine engine;

    public DeathMatchManager(Engine engine, int numOfTeams, int maxDeaths)
    {
        this.engine = engine;
        livesLeft = new int[numOfTeams];
        livesLeftText = new TextRender[numOfTeams];

        if (numOfTeams == 2)
        {
            String maxDeathsString = String.valueOf(maxDeaths);
            livesLeftText[0] = EntityFactory.createText(maxDeathsString,
                    new Vector2(Constants.UI_PADDING + 45, Gdx.graphics.getHeight() / 2 + 45),
                    new Color(1, 127f/255f, 127f/255f, 1), 1)
                        .getComponent(TextRender.class);
            livesLeftText[1] = EntityFactory.createText(maxDeathsString, new Vector2(
                    Gdx.graphics.getWidth() - Constants.UI_PADDING - 45, Gdx.graphics.getHeight() / 2 + 45),
                    new Color(110f/255f, 200f/255f, 230f/255f, 1), 1)
                        .getComponent(TextRender.class);
            livesLeft[0] = maxDeaths;
            livesLeft[1] = maxDeaths;
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

            EntityFactory.createText("Team " + winner + " wins!",
                    new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), Color.WHITE, .4f);

            AssetLoader.fadeMusic();
        }

        livesLeftText[teamNum].text = String.valueOf(livesLeft[teamNum]);
    }
}
