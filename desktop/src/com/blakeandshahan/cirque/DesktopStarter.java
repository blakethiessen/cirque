package com.blakeandshahan.cirque;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.blakeandshahan.cirque.utils.ImagePacker;

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

        // Pass in the argument "pack" to run the image packer.
        if (args.length > 0)
        {
            for (String arg : args)
            {
                if (arg.equals("pack"))
                    ImagePacker.run();
                if (arg.equals("f"))
                {
                    cfg.width = (int)screenSize.getWidth();
                    cfg.height = (int)screenSize.getHeight();
                    cfg.fullscreen = true;
                }
            }
        }

        new LwjglApplication(new Cirque(), cfg);
    }
}
