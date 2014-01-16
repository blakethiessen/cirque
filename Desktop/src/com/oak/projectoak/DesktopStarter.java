package com.oak.projectoak;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oak.projectoak.utils.ImagePacker;

/*
    DesktopStarter initializes the game on desktop.
 */

public class DesktopStarter
{
    public static final int FRAME_WIDTH = 1280;
    public static final int FRAME_HEIGHT = 720;

    public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Project Oak";
        cfg.width = FRAME_WIDTH;
        cfg.height = FRAME_HEIGHT;
        cfg.useGL20 = true;
        cfg.vSyncEnabled = true;

        // Uncomment the line below when adding new sprites.
        ImagePacker.run();
        new LwjglApplication(new ProjectOak(), cfg);
    }
}
