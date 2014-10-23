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


public class ScoreTrackingSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;

    public Entity x,y,z;
    private boolean init, change;
    private int p0,p1,p2,p3;
    private float w;
    public final GameModeManager gmManager;

    public ScoreTrackingSystem(GameModeManager gmManager)
    {
        super(Aspect.getAspectForAll(Player.class));
        this.gmManager = gmManager;

        init = true;
        change = false;
        p0=p1=p2=p3=0;
        w = new BitmapFont().getBounds("Remaining Lives").width;
    }

    //SHOULD WE DO RUN processSystem()??
    @Override
    protected boolean checkProcessing()
    {
        return !gmManager.isGameOver();
    }

    @Override
    protected void process(Entity e)
    {
        //Grab the player entity and determine its team number
        Player p = pm.get(e);

        if(init)
        {
            x =   EntityFactory.createText(world,
                                         "Remaining Lives",
                                          new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2),
                                          Color.WHITE,
                                          w,
                                          BitmapFont.HAlignment.CENTER);

            y =   EntityFactory.createText(world,
                    String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN),
                    new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2 - 16),
                    Color.RED,
                    w,
                    BitmapFont.HAlignment.LEFT);

            z =   EntityFactory.createText(world,
                    String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN),
                    new   Vector2(Gdx.graphics.getWidth()/2-  w/2, Gdx.graphics.getHeight()/2 - 16),
                    Color.BLUE,
                    w,
                    BitmapFont.HAlignment.RIGHT);


            init = false;
        }


        if(p.playerNum == 0 && p.deaths != p0)
        {
            p0 = p.deaths;
            change = true;
        }
        else if(p.playerNum == 1 && p.deaths != p1)
        {
            p1 = p.deaths;
            change = true;
        }
        else if(p.playerNum == 2 && p.deaths != p2)
        {
            p2 = p.deaths;
            change = true;
        }
        else if(p.playerNum == 3 && p.deaths != p3)
        {
            p3 = p.deaths;
            change = true;
        }

        if(change)
        {
            y.getComponent(TextRender.class).text = String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p0 - p3);
            z.getComponent(TextRender.class).text = String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p1 - p2);
            change = false;
        }

        //DISPLAY GAME OVER / RESTART TEXT


    }




}
