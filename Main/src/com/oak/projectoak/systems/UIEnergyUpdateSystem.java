package com.oak.projectoak.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.oak.projectoak.components.Player;
import com.oak.projectoak.components.TextRender;

public class UIEnergyUpdateSystem extends EntityProcessingSystem
{
    @Mapper ComponentMapper<Player> pm;
    @Mapper ComponentMapper<TextRender> trm;

    public UIEnergyUpdateSystem()
    {
        super(Aspect.getAspectForAll(Player.class, TextRender.class));
    }

    @Override
    protected void process(Entity e)
    {
        Player player = pm.get(e);
        TextRender textRender = trm.get(e);

        textRender.text = "Player " + player.playerNum + " energy: " + player.energyAmt;
    }
}
