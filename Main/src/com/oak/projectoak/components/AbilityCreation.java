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

    public AbilityCreation(AbilityType abilityType)
    {
        this.abilityType = abilityType;
        justUsed = false;

        switch (abilityType)
        {
            case STAKE:
            case PILLAR:
                energyAmt = .33f;
                energyCostPerUse = Constants.STAKE_ENERGY_COST;
                energyIncreasePerFrame = Constants.ENERGY_INCREASE_PER_FRAME_OF_RUNNING;
                break;
        }
    }

    public boolean enoughEnergyForUse()
    {
        return energyAmt >= energyCostPerUse;
    }
}
