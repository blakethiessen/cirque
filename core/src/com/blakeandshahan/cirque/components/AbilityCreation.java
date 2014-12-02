package com.blakeandshahan.cirque.components;

import com.badlogic.ashley.core.Component;
import com.blakeandshahan.cirque.AbilityType;
import com.blakeandshahan.cirque.Constants;

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

        resetAbility();
    }

    public void resetAbility()
    {
        numUsesAvailable = 0;

        switch (abilityType)
        {
            case STAKE:
            case PILLAR:
                energyAmt = Constants.STARTING_TIER1_ABILITY_ENERGY;
                energyCostPerUse = Constants.TIER1_ABILITY_ENERGY_COST;
                energyIncreasePerFrame = Constants.TIER1_ENERGY_INCREASE_PER_FRAME_OF_RUNNING;
                break;
            case LIGHTNING_BOLT:
                energyAmt = Constants.STARTING_TIER1_ABILITY_ENERGY;
                energyCostPerUse = Constants.LIGHTNING_BOLT_ENERGY_COST;
                energyIncreasePerFrame = Constants.TIER1_ENERGY_INCREASE_PER_FRAME_OF_RUNNING;
                break;
            default:
        }
    }

    public boolean enoughEnergyForUse()
    {
        return energyAmt >= energyCostPerUse;
    }
}
