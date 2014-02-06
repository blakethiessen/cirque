package com.oak.projectoak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oak.projectoak.utils.ImagePacker;

import java.awt.*;

/*
    DesktopStarter initializes the game on desktop.
 */

public class DesktopStarter
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Project Oak";
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cfg.width = 1280;
        cfg.height = 720;
//        cfg.width = (int)screenSize.getWidth();
//        cfg.height = (int)screenSize.getHeight();
        cfg.useGL20 = true;
        cfg.vSyncEnabled = true;
//        cfg.fullscreen = true;

        // Uncomment the line below when adding new sprites.
        ImagePacker.run();
        new LwjglApplication(new ProjectOak(), cfg);
    }
}
