package com.oak.projectoak.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GraphicsDebugSystem extends EntitySystem
{
    private Camera camera;
    private static ArrayList<DebugDrawable> debugDrawables;

    public GraphicsDebugSystem(Camera camera)
    {
        this.camera = camera;
        debugDrawables = new ArrayList<DebugDrawable>();
    }

    @Override
    public void update(float deltaTime)
    {
        if (!debugDrawables.isEmpty())
        {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (DebugDrawable debugDrawable : debugDrawables)
            {
                shapeRenderer.identity();
                shapeRenderer.setColor(debugDrawable.color);
                shapeRenderer.translate(debugDrawable.twoDPosition.x, debugDrawable.twoDPosition.y, 0);
                shapeRenderer.rotate(0, 0, 1, debugDrawable.rotation);
                shapeRenderer.rect(0, 0,
                        debugDrawable.width, debugDrawable.height);
            }

            shapeRenderer.end();
        }
    }

    public static void drawDebugRectangle(Vector2 twoDPosition, float width, float height, float rotation, Color color, int drawTimeMs)
    {
        debugDrawables.add(new DebugDrawable(twoDPosition.scl(Constants.METERS_TO_PIXELS),
                Constants.ConvertMetersToPixels(width), Constants.ConvertMetersToPixels(height), (float)Math.toDegrees(rotation), color, drawTimeMs));
    }

    private static class DebugDrawable
    {
        public final Vector2 twoDPosition;
        public final float width;
        public final float height;
        public final Color color;
        private final float rotation;

        public DebugDrawable(Vector2 twoDPosition, float width, float height, float rotation, Color color, int drawTime)
        {
            this.twoDPosition = twoDPosition;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
            this.color = color;

            if (drawTime != 0)
            {
                Timer timer = new Timer();

                final DebugDrawable that = this;

                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        debugDrawables.remove(that);
                    }
                }, drawTime);
            }
        }
    }
}
