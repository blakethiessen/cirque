package com.oak.projectoak.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.oak.projectoak.Constants;

public class RenderOffset extends Component
{
    public Vector2 pxOffset;

    public RenderOffset(Vector2 meterOffset)
    {
        this.pxOffset = meterOffset.scl(Constants.METERS_TO_PIXELS);
    }
}
