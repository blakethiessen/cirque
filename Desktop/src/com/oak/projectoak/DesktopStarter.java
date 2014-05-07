package com.oak.projectoak;

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
        cfg.title = "Cirque";
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cfg.width = 1280;
        cfg.height = 720;
        cfg.useGL20 = true;
        cfg.vSyncEnabled = true;

        for (int i = 0; i < args.length; i++)
        {
            // go fullscreen
            if (args[i].equals("f"))
            {
                cfg.width = (int)screenSize.getWidth();
                cfg.height = (int)screenSize.getHeight();
                cfg.fullscreen = true;
            }
            // Pack images
            else if (args[i].equals("pack"))
            {
                ImagePacker.run();
            }
        }

        new LwjglApplication(new ProjectOak(), cfg);
    }
}
