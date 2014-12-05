package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Constants;

public class RenderOffset extends Component implements Pool.Poolable
{
    public Vector2 pxOffset;

    public RenderOffset init(Vector2 meterOffset)
    {
        this.pxOffset = meterOffset.scl(Constants.METERS_TO_PIXELS);

        return this;
    }

    @Override
    public void reset() {}
}
