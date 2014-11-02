package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.blakeandshahan.cirque.Constants;

public class RenderOffset extends Component
{
    public Vector2 pxOffset;

    public RenderOffset(Vector2 meterOffset)
    {
        this.pxOffset = meterOffset.scl(Constants.METERS_TO_PIXELS);
    }
}
