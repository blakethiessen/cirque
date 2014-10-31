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
        cfg.vSyncEnabled = true;

        // Pass in the argument "nopack" to not run the image packer.
        if (args.length > 0)
        {
            if (!args[0].equals("nopack"))
                ImagePacker.run();
            if (args.length > 1 && args[1].equals("f"))
            {
                cfg.width = (int)screenSize.getWidth();
                cfg.height = (int)screenSize.getHeight();
                cfg.fullscreen = true;
            }
        }
        else
            ImagePacker.run();


        new LwjglApplication(new ProjectOak(), cfg);
    }
}
