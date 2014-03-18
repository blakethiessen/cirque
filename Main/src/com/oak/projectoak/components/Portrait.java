package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;

/**
 * Created by SSK on 3/18/14.
 */
public class Portrait extends Component
{

    public String portraitName;
    public int teamNum;

    public Portrait(String portraitName, int teamNum)
    {
        this.portraitName = portraitName;
        this.teamNum = teamNum;
    }

}
