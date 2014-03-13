package com.oak.projectoak.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.oak.projectoak.Constants;

import java.util.ArrayList;

public class Pillar extends Component
{
    public ArrayList<Entity> pillarsStackedOnTop;
    public int destructionTimeReset;

    public Pillar()
    {
        pillarsStackedOnTop = new ArrayList<Entity>(5);
        destructionTimeReset = Constants.PILLAR_DESTRUCTION_TIME_RESET;
    }
}
