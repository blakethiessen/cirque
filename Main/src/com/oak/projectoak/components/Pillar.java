package com.oak.projectoak.components;

import com.artemis.Component;
import com.oak.projectoak.Constants;

import java.util.LinkedList;
import java.util.Queue;

/*
    - A pillar entity is a collection of pieces.
    - Adding a new pillar increments a counter, which is updated.
    - A single body is resized to contain all pieces.
    - A timer is used to remove the top pillar piece.
 */

public class Pillar extends Component
{
    public int numOfPillarSegments;
    public int prevNumOfPillarSegments;

    public Queue<Long> pillarTimeouts;

    public Pillar()
    {
        numOfPillarSegments = 1;
        prevNumOfPillarSegments = 1;

        pillarTimeouts = new LinkedList<Long>();
        pillarTimeouts.add(System.currentTimeMillis() + Constants.PILLAR_LIFETIME_MS);
    }
}
