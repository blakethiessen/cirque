package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.oak.projectoak.AssetLoader;
import com.oak.projectoak.Constants;
import com.oak.projectoak.components.AbilityCreation;
import com.oak.projectoak.components.Animate;
import com.oak.projectoak.components.Render;

public class UIEnergyUpdateSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<AbilityCreation> acm;
    @Mapper ComponentMapper<Render> rm;
    @Mapper ComponentMapper<Animate> am;

    public UIEnergyUpdateSystem()
    {
        super(Aspect.getAspectForAll(AbilityCreation.class));
    }

    @Override
    protected void process(Entity e)
    {
        AbilityCreation abilityCreation = acm.get(e);
        Render render = rm.get(e);
        Animate animate = am.get(e);

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
