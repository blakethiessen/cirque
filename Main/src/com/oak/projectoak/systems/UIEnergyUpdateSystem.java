package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

//        if (abilityCreation.numUsesAvailable < Math.floor(abilityCreation.energyAmt / abilityCreation.energyCostPerUse))
//        {
//            render.sprites[3].setScale(1);
//            Constants.setSpriteTexture(render.sprites[3], AssetLoader.getAnimation(Constants.UI_ENERGY_READY).getKeyFrame(0));
//            abilityCreation.numUsesAvailable++;
//        }

//        if (animate.getAnimation() != null && animate.getAnimation().isAnimationFinished(animate.stateTime))
//        {
//            render.sprites[3].setScale(0);
//            animate.stateTime = 0;
//        }
    }
}
