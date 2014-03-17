package com.oak.projectoak.components;

import com.artemis.Component;
import com.oak.projectoak.AbilityType;
import com.oak.projectoak.Constants;

public class AbilityCreation extends Component
{
    public AbilityType abilityType;

    public float energyAmt;
    public float energyCostPerUse;
    public float energyIncreasePerFrame;
    public boolean justUsed;

    public int numUsesAvailable;

    public AbilityCreation(AbilityType abilityType)
    {
        this.abilityType = abilityType;
        justUsed = false;

        numUsesAvailable = 0;

        switch (abilityType)
        {
            case STAKE:
            case PILLAR:
                energyAmt = Constants.STARTING_TIER1_ABILITY_ENERGY;
                energyCostPerUse = Constants.TIER1_ABILITY_ENERGY_COST;
//                energyCostPerUse = 0;
                energyIncreasePerFrame = Constants.TIER1_ENERGY_INCREASE_PER_FRAME_OF_RUNNING;
                break;
        }
    }

    public boolean enoughEnergyForUse()
    {
        return energyAmt >= energyCostPerUse;
    }
}
