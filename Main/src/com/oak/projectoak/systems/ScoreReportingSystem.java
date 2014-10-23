package com.oak.projectoak.systems;


import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.TextRender;
import com.oak.projectoak.entity.EntityFactory;
import com.oak.projectoak.gamemodemanagers.GameModeManager;

import javax.xml.soap.Text;

public class ScoreReportingSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<TextRender> tm;

    private final GameModeManager gmManager;

    Entity x,y,z;


    float w;
    boolean change, init;




    private final String[] characterNames = {"Pirate    :","Ninja      :","Gangsta :","Pharaoh:"};
    private int p0Deaths, p1Deaths, p2Deaths, p3Deaths;

    public ScoreReportingSystem(GameModeManager gmManager)
    {
        super(Aspect.getAspectForAll(Player.class));
        this.gmManager = gmManager;


        w = new BitmapFont().getBounds("Remaining Lives").width;
        init = true;
        change = false;
        p0Deaths = p1Deaths = p2Deaths = p3Deaths = 0;

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
        //Grab the player entity
        Player p = pm.get(e);

        if(init)
        {
            x = EntityFactory.createText(world,
                    "Remaining Lives",
                    new Vector2(Gdx.graphics.getWidth() / 2 - w / 2, Gdx.graphics.getHeight() / 2),
                    Color.WHITE,
                    w,
                    BitmapFont.HAlignment.CENTER);

            //Show red team remaining lives
            y = EntityFactory.createText(world,
                    String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p0Deaths - p2Deaths),
                    new Vector2(Gdx.graphics.getWidth() / 2 - w / 2, Gdx.graphics.getHeight() / 2 - 16),
                    Color.RED,
                    w,
                    BitmapFont.HAlignment.LEFT);


            //Show blue team remaining lives
            z = EntityFactory.createText(world,
                    String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p1Deaths - p3Deaths),
                    new Vector2(Gdx.graphics.getWidth() / 2 - w / 2, Gdx.graphics.getHeight() / 2 - 16),
                    Color.BLUE,
                    w,
                    BitmapFont.HAlignment.RIGHT);

            init = false;
        }


        //Update player deaths
        if(p.playerNum == 0)
        {
            if(p.deaths != p0Deaths)
            {
                p0Deaths = p.deaths;
                change = true;
            }
        }
        else if(p.playerNum == 1)
        {
            if(p.deaths != p1Deaths)
            {
                p1Deaths = p.deaths;
                change = true;
            }
        }
        else if(p.playerNum == 2)
        {
            if(p.deaths != p2Deaths)
            {
                p2Deaths = p.deaths;
                change = true;
            }
        }
        else if(p.playerNum == 3)
        {
            if(p.deaths != p3Deaths)
            {
                p3Deaths = p.deaths;
                change = true;
            }
        }




        if(change)
        {
            y.getComponent(TextRender.class).text = String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p0Deaths - p2Deaths);
            z.getComponent(TextRender.class).text = String.valueOf(Constants.DEATHMATCH_KILLS_TO_WIN - p1Deaths - p3Deaths);
            change = false;
        }

    }




}
