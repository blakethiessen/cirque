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
import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;



//Tracks score throughout the game's execution
public class ScoreTrackingSystem extends EntityProcessingSystem implements InputProcessor
{
    @Mapper ComponentMapper<Player> pm;

    //a private class to easily manage player information. Should think about making this public and have the Player class use it as well.
    private class PlayerInfo
    {
        public int deaths, kills, friendlyKills, teamNum;
        public String playerName;

        public PlayerInfo()
        {
            deaths = kills = friendlyKills = teamNum = 0;
            playerName = "N / A";
        }
    }

    public Entity x,y,z;
    private boolean init, change, finalize;
    private final int offset = -70;
    private final int heightDifference = -16;
    public float w;                             //gets updated to max length in pixels (bitmap font) when getTeamStats() is called
    public final GameModeManager gmManager;


    private ArrayList<PlayerInfo> players;
    private ArrayList<String>  teamColors;     //The team colors. Currently there are 4 in this list by default.
    private ArrayList<Entity> teamScoreList; //these are the text objects that will display team scores in the end


    private final GameScreen gameScreen;
    private final Game game;
    private final OrthographicCamera camera;

    private int totalTeams;
    private int playersPerTeam;


    //These should probably be in a different system



    public ScoreTrackingSystem(GameModeManager gmManager,GameScreen gameScreen, Game game, OrthographicCamera camera, int totalTeams, int playersPerTeam)
    {
        super(Aspect.getAspectForAll(Player.class));
        this.gmManager = gmManager;
        this.gameScreen = gameScreen;
        this.game = game;
        this.camera = camera;
        this.totalTeams = totalTeams;
        this.playersPerTeam = playersPerTeam;

        init = change = true;
        finalize = false;


        //Get the size of the text that we will align the Score in the middle to
        w = new BitmapFont().getBounds("Remaining Lives").width;
    }


    @Override
    protected void process(Entity e)
    {
        //Grab the player entity and determine its team number
        Player p = pm.get(e);

        if(init)
        {
            init(); //call the init function
            init = false;   //turn init to false so we dont call it again
        }

        //updates info related to this player . If an update is made to this player's deaths. The change variable is set to true.
        updatePlayerInfo(p);

        //Display remaining lives if game isnt over
        if(change && !gmManager.isGameOver())
        {
           displayLivesRemaining();
           change = false;
        }

        //Display Final Scores if game is over
        if(gmManager.isGameOver())
        {
            if(!finalize)
            {
                createScores();
                Gdx.input.setInputProcessor(this);
                finalize = true;
            }
            else if(change)
            {
                displayScores();
                change =  false;
            }
        }



    }




    private void init()
    {
        x =   EntityFactory.createText(world,
                "Remaining Lives",
                new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2),
                Color.WHITE,
                w,
                BitmapFont.HAlignment.CENTER);

        y =   EntityFactory.createText(world,
                String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN),
                new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2 + heightDifference),
                Color.RED,
                w,
                BitmapFont.HAlignment.LEFT);

        z =   EntityFactory.createText(world,
                String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN),
                new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2 + heightDifference),
                Color.BLUE,
                w,
                BitmapFont.HAlignment.RIGHT);

        //initialize all the lists.
        players = new ArrayList<PlayerInfo>();

        for(int i = 0; i < playersPerTeam * totalTeams; i++)
        {
            players.add(new PlayerInfo());
        }

        teamColors      = new ArrayList<String>();
        populateColors();

        teamScoreList = null;

    }


    private void displayLivesRemaining()
    {
        y.getComponent(TextRender.class).text = String.valueOf(getLivesRemaining(0));
        z.getComponent(TextRender.class).text = String.valueOf(getLivesRemaining(1));
    }

    private void createScores()
    {
        //No need to display remaining lives anymore. Uses y as gameover.
        z.getComponent(TextRender.class).text = "";

        //create TEAM score entities with textrender components cause they dont exist yet
        if(teamScoreList == null)
        {
            int totalPlayers = playersPerTeam * totalTeams;
            teamScoreList = new ArrayList<Entity>();
            for(int i = 0; i < Constants.DEATHMATCH_NUM_TEAMS; i++)
            {

                teamScoreList.add( EntityFactory.createText(world,
                                   getTeamStats(i),    //Updates w             //height difference magically works here for some reason to align in the center of the circle
                                   new Vector2(Gdx.graphics.getWidth()/2 + w + heightDifference, Gdx.graphics.getHeight()/2 - (offset) + (playersPerTeam * heightDifference * (i+1)    )),
                                   getColor(teamColors.get(i)),
                                   0,
                                   BitmapFont.HAlignment.RIGHT )               //using all the lines text align nicely

                                  );

                //we can add the final game over text in our last iteration of the loop
                if( i == Constants.DEATHMATCH_NUM_TEAMS - 1)
                {
                    y.getComponent(TextRender.class).alignmentSize = w;
                    y.getComponent(TextRender.class).alignment = BitmapFont.HAlignment.CENTER;
                    y.getComponent(TextRender.class).color = Color.WHITE;
                    y.getComponent(TextRender.class).position = new Vector2(Gdx.graphics.getWidth()/2 - w/2, Gdx.graphics.getHeight()/2 - (offset) + (playersPerTeam * heightDifference * (i+3)    ));
                    y.getComponent(TextRender.class).text = Constants.GAME_OVER_TEXT;
                }
            }
        }

        //Now change alignment size of each object
        for(Entity t : teamScoreList)
                t.getComponent(TextRender.class).alignmentSize = w;

        //DISPLAY win message, use w to center text horizontally. Will need to figure out how many players there were. Each takes up a line.
        String winMessage =  teamColors.get(getWinningTeam()) + " Team Wins!";
        x.getComponent(TextRender.class).alignmentSize = w;
        x.getComponent(TextRender.class).position = new Vector2(Gdx.graphics.getWidth()/2 - w/2, Gdx.graphics.getHeight()/2 - offset);
        x.getComponent(TextRender.class).text = winMessage;
    }


    private void displayScores()
    {
        for(int i = 0; i < Constants.DEATHMATCH_NUM_TEAMS; i++)
            teamScoreList.get(i).getComponent(TextRender.class).text = getTeamStats(i);
    }


    private void populateColors()
    {
        teamColors.add("RED");
        teamColors.add("BLUE");
        teamColors.add("GREEN");
        teamColors.add("YELLOW");
    }

    private Color getColor(String s)
    {
        Color color;
        try
        {
            Field field = Color.class.getField(s.toUpperCase());
            color = (Color)field.get(null);
        }
        catch (Exception e)
        {
            color = null; // Not defined
        }

        if(color != null)
            return color;

        //return a random color if there's no color defined by that string
        Random rand = new Random(255);

        int r = rand.nextInt();         if(r < 128)  r+= 128;
        int g = rand.nextInt();         if(g < 128)  g+= 128;
        int b = rand.nextInt();         if(b < 128)  b+= 128;

        return new Color(r,g,b,1);
    }


    private void updatePlayerInfo(Player p)
    {
        int index = p.playerNum;

        //Update player deaths
        if(players.get(index).deaths  !=  p.deaths)
        {
           players.get(index).deaths = p.deaths;
           change = true;
        }

        //Update player kills
        if(players.get(index).kills != p.enemyKills)
            players.get(index).kills = p.enemyKills;

        //Update player friendly kills
        if(players.get(index).friendlyKills != p.friendlyKills)
            players.get(index).friendlyKills = p.friendlyKills;

        //Update player team        -- we will need this compute LIVES remaining. Will only be true once.
        if(players.get(index).teamNum != p.teamNum)
            players.get(index).teamNum =p.teamNum;

        //Update player names       -- Will only be true once
        if(!players.get(index).playerName.equals(p.playerName))
            players.get(index).playerName = p.playerName;
    }


    //Returns the lives remaining of this team
    private int getLivesRemaining(int teamNumber)
    {
        int livesRemaining = Constants.DEATHMATCH_KILLS_TO_WIN;

        //iterate over the list that has the team numbers of each player
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).teamNum == teamNumber)
                livesRemaining -= players.get(i).deaths;
        }

        return livesRemaining;
    }


    //Returns a player's stats
    private String getPlayerStats(int playerNum)
    {
        //new BitmapFont().getBounds("Remaining Lives").width;
        String s = "No such player : " + playerNum;

        if(playerExists(playerNum))
            s = players.get(playerNum).playerName + "     EnemyKills : " + players.get(playerNum).kills
              + "     Friendly Kills : " + players.get(playerNum).friendlyKills + "     Deaths : " + players.get(playerNum).deaths;

        Math.max(w,new BitmapFont().getBounds(s).width);    //update the max Width

        return s;
    }


    //Determines whether this player exists. Prevents crash.
    private boolean playerExists(int playerNum)
    {
        if(playerNum < players.size())
            return true;
        return false;
    }

    //Returns a string with each player's stats on the team separated by '\n'
    public String getTeamStats(int teamNum)
    {
        String ret = "";
        //iterate over teams
        for(int i = 0; i < players.size(); i++)
        {
            //if player[i] belongs to teamNum, then add his stats
            if(players.get(i).teamNum == teamNum)
                ret +=   getPlayerStats(i) + "\n";
        }

        //now remove the final "\n" and return it
        return ret.substring(0,ret.length() - 1);
    }


    //Returns the teamNum of the team who won. Determined by the least deaths or the most Lives Remaining.
    public int getWinningTeam()
    {
        int ret = 0;
        int deaths = Constants.DEATHMATCH_KILLS_TO_WIN;

        //iterate over all teams
        for(int i = 0; i < Constants.DEATHMATCH_NUM_TEAMS; i++)
        {
            int tempDeaths = getLivesRemaining(i);     //get lives remaining of the current team
            if(tempDeaths < deaths)
            {
                ret = i;
                deaths = tempDeaths;
            }
        }

        return ret;
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
