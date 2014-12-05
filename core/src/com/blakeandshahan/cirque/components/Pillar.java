package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.blakeandshahan.cirque.Constants;

import java.util.LinkedList;
import java.util.Queue;

/*
    - A pillar entity is a collection of pieces.
    - Adding a new pillar increments a counter, which is updated.
    - A single body is resized to contain all pieces.
    - A timer is used to remove the top pillar piece.
 */

public class Pillar extends Component implements Pool.Poolable
{
    public int numOfPillarSegments;
    public int prevNumOfPillarSegments;

    public Queue<Long> pillarTimeouts;

    public Pillar init()
    {
        numOfPillarSegments = 1;
        prevNumOfPillarSegments = 1;

        pillarTimeouts = new LinkedList<Long>();
        pillarTimeouts.add(System.currentTimeMillis() + Constants.PILLAR_LIFETIME_MS);

        return this;
    }

    @Override
    public void reset() {}
}
