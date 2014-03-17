package com.oak.projectoak.systems;


import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.TextRender;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.gamemodemanagers.GameModeManager;
import com.oak.projectoak.screens.GameScreen;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

public class GameOverSystem extends EntityProcessingSystem implements InputProcessor
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<TextRender> tm;

    private final GameModeManager gmManager;
    private ScoreTrackingSystem scoreTracking;
    private final GameScreen gameScreen;
    private final Game game;
    private final OrthographicCamera camera;
    private final int heightDifference = -16;
    private final int maxPlayersPerTeam = 2;


    private final String[] characterNames = {"Pirate    :","Ninja      :","Gangsta :","Pharaoh:"};

    private boolean hasRun;
    private int playerNum, redTeamDeaths, blueTeamDeaths;

    String winMessage;
    private List<String> blueTeamList;
    private List<String> redTeamList;

    public GameOverSystem(GameModeManager gmManager, ScoreTrackingSystem scoreTracking, GameScreen gameScreen, Game game, OrthographicCamera camera)
    {
        super(Aspect.getAspectForAll(Player.class));
        this.gmManager = gmManager;
        this.scoreTracking = scoreTracking;
        this.gameScreen = gameScreen;
        this.game = game;
        this.camera = camera;

        hasRun = false;
        playerNum = 1;

        redTeamDeaths = blueTeamDeaths = 0;
        redTeamList = new ArrayList<String>();
        blueTeamList = new ArrayList<String>();

    }

    //SHOULD WE DO RUN processSystem()??
    @Override
    protected boolean checkProcessing()
    {
        return !hasRun && gmManager.isGameOver();
    }

    @Override
    protected void process(Entity e)
    {
        //Grab the player entity and determine its team number
        Player p = pm.get(e);

        //BUILD STRING UP
        //make player strings by player number
        if(p.teamNum == 0)
        {
            redTeamList.add(characterNames[playerNum - 1] + "     EnemyKills : " + p.enemyKills + "     Friendly Kills : " + p.friendlyKills + "     Deaths : " + p.deaths);
            redTeamDeaths += p.deaths;
        }
        else
        {
            blueTeamList.add(characterNames[playerNum - 1] + "     EnemyKills : " + p.enemyKills + "     Friendly Kills : " + p.friendlyKills + "     Deaths : " + p.deaths);
            blueTeamDeaths += p.deaths;
        }

        playerNum++;

        //Now that we have gathered all the info for the players, we can determine who won and then show the scoreboard and then center it.
        if(playerNum == 2 * maxPlayersPerTeam + 1)
        {
            //determine who won by deaths. Whoever died the most lost

            TextRender textRender = tm.get(scoreTracking.x);
            textRender.text = "";
            TextRender textRender2 = tm.get(scoreTracking.y);
            textRender2.text = "";
            TextRender textRender3 = tm.get(scoreTracking.z);
            textRender3.text = "";

            if(redTeamDeaths > blueTeamDeaths)
                winMessage = "Blue Team Wins!";
            else
                winMessage = "Red Team Wins!";


            //Need to determine width and height of all the text we are going to use
            int maxWidth =  getMaxWidthOfText();



            //DISPLAY win message, use maxWidth to center text horizontally
            EntityFactory.createText(world,
                    winMessage,
                    new Vector2(Gdx.graphics.getWidth()/2 - maxWidth/2, Gdx.graphics.getHeight()/2 - ((maxPlayersPerTeam + 2) * heightDifference) +  (heightDifference * 0)),
                    Color.WHITE,
                    (float)maxWidth,
                    BitmapFont.HAlignment.CENTER );

            //DISPLAY RED TEAM SCORES
            for(int i = 0 ; i < redTeamList.size(); i++)
            {
                EntityFactory.createText(world,
                                         redTeamList.get(i),
                                         new Vector2(Gdx.graphics.getWidth()/2 - maxWidth/2,Gdx.graphics.getHeight()/2 - ((maxPlayersPerTeam + 2) * heightDifference)+  (heightDifference * (i+1))),
                                         Color.RED ,
                                         (float)maxWidth,
                                         BitmapFont.HAlignment.CENTER);
            }




            //DISPLAY BLUE TEAM SCORES
            for(int i = 0 ; i < blueTeamList.size(); i++)
            {
                EntityFactory.createText(world,
                                         blueTeamList.get(i),
                                         new Vector2(Gdx.graphics.getWidth()/2 - maxWidth/2,Gdx.graphics.getHeight()/2 - ((maxPlayersPerTeam + 2) * heightDifference)+  (heightDifference * (maxPlayersPerTeam + i+1))),
                                         Color.BLUE ,
                                         (float)maxWidth,
                                         BitmapFont.HAlignment.CENTER);
            }

            //DISPLAY GAME OVER / RESTART TEXT
            EntityFactory.createText(world,
                                     Constants.GAME_OVER_TEXT,
                                     new Vector2(Gdx.graphics.getWidth()/2 - maxWidth/2, Gdx.graphics.getHeight()/2 - ((maxPlayersPerTeam + 2) * heightDifference)+  heightDifference * ((2 * maxPlayersPerTeam + 1))) ,
                                     Color.WHITE,
                                     (float)maxWidth,
                                     BitmapFont.HAlignment.CENTER);
        }
    }

    private int getMaxWidthOfText()
    {
        BitmapFont b = new BitmapFont();

        //need to iterate thru all our strings to find out the max width
        int maxWidth = (int)b.getBounds(winMessage).width;

        //game over
        maxWidth = Math.max(maxWidth, (int)b.getBounds(Constants.GAME_OVER_TEXT).width);

        //team red
        for(String s : redTeamList)
            maxWidth = Math.max(maxWidth, (int)b.getBounds(s).width);

        //team blue
        for(String s : blueTeamList)
            maxWidth = Math.max(maxWidth, (int)b.getBounds(s).width);

        return maxWidth;
    }

    @Override
    protected void end()
    {
        hasRun = true;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.ENTER)
        {
            world.setSystem(new CameraZoomTransitionSystem(camera, 0, game, gameScreen, true));
        }
        else if (keycode == Input.Keys.ESCAPE)
        {
            world.setSystem(new CameraZoomTransitionSystem(camera, 0, game, gameScreen, false));
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
