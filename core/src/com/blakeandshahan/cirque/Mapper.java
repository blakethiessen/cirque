package com.blakeandshahan.cirque;

import com.badlogic.ashley.core.ComponentMapper;
import com.blakeandshahan.cirque.components.*;
import com.blakeandshahan.cirque.components.physics.ArenaTransform;
import com.blakeandshahan.cirque.components.physics.DynamicPhysics;
import com.blakeandshahan.cirque.components.physics.Physics;
import com.blakeandshahan.cirque.components.physics.TrapPhysics;

public class Mapper
{
    public static final ComponentMapper<ArenaTransform> arenaTransform = ComponentMapper.getFor(ArenaTransform.class);
    public static final ComponentMapper<DynamicPhysics> dynamicPhysics = ComponentMapper.getFor(DynamicPhysics.class);
    public static final ComponentMapper<Physics> physics = ComponentMapper.getFor(Physics.class);
    public static final ComponentMapper<TrapPhysics> trapPhysics = ComponentMapper.getFor(TrapPhysics.class);
    public static final ComponentMapper<Ability> ability = ComponentMapper.getFor(Ability.class);
    public static final ComponentMapper<AbilityCreation> abilityCreation =
            ComponentMapper.getFor(AbilityCreation.class);
    public static final ComponentMapper<Animate> animate = ComponentMapper.getFor(Animate.class);
    public static final ComponentMapper<ArenaRotation> arenaRotation = ComponentMapper.getFor(ArenaRotation.class);
    public static final ComponentMapper<Pillar> pillar = ComponentMapper.getFor(Pillar.class);
    public static final ComponentMapper<Platformer> platformer = ComponentMapper.getFor(Platformer.class);
    public static final ComponentMapper<Player> player = ComponentMapper.getFor(Player.class);
    public static final ComponentMapper<PlayerAnimation> playerAnimation =
            ComponentMapper.getFor(PlayerAnimation.class);
    public static final ComponentMapper<Portrait> portrait = ComponentMapper.getFor(Portrait.class);
    public static final ComponentMapper<Render> render = ComponentMapper.getFor(Render.class);
    public static final ComponentMapper<RenderOffset> renderOffset = ComponentMapper.getFor(RenderOffset.class);
    public static final ComponentMapper<TextRender> textRender = ComponentMapper.getFor(TextRender.class);
    public static final ComponentMapper<UI> ui = ComponentMapper.getFor(UI.class);
}
