package com.oak.projectoak.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.Mapper;
import com.oak.projectoak.components.AbilityCreation;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Render;

public class UIEnergyUpdateSystem extends IteratingSystem
{
    public UIEnergyUpdateSystem()
    {
        super(Family.getFor(AbilityCreation.class));
    }

    @Override
    protected void processEntity(Entity e, float deltaTime)
    {
        AbilityCreation abilityCreation = Mapper.abilityCreation.get(e);
        Render render = Mapper.render.get(e);
        Animate animate = Mapper.animate.get(e);

        Sprite energyLevelSprite = render.sprites[1];

        energyLevelSprite.setRegionHeight((int)(energyLevelSprite.getHeight() * abilityCreation.energyAmt));
        energyLevelSprite.setScale(1 , -abilityCreation.energyAmt);
        energyLevelSprite.setY(render.sprites[0].getY() - ((1 - abilityCreation.energyAmt) * energyLevelSprite.getHeight()) / 2);

        if (abilityCreation.numUsesAvailable < Math.floor(abilityCreation.energyAmt / abilityCreation.energyCostPerUse))
        {
            animate.setAnimation(Constants.UI_ENERGY_READY);
            AssetLoader.playSound("ability_ready");
            abilityCreation.numUsesAvailable++;
        }
    }
}
