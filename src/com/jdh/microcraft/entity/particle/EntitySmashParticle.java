package com.jdh.microcraft.entity.particle;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.level.Level;

public class EntitySmashParticle extends EntityParticle {
    private static final int SPRITE_X = 12, SPRITE_Y = 2;

    private final int color;

    public EntitySmashParticle(Level level, int x, int y, int color) {
        super(level, x, y);
        this.color = color;
    }

    public static void spawn(Level level, int x, int y, int color, int min, int max) {
        int n = min + Global.random.nextInt(max - min + 1);
        for (int i = 0; i < n; i++) {
            level.addEntity(new EntitySmashParticle(level, x, y, color));
        }
    }

    @Override
    public void render() {
        Global.random.setSeed(this.id);
        Renderer.render(
            SPRITE_X, SPRITE_Y, this.getRenderX(), this.getRenderY(), color,
            (Global.random.nextBoolean() ? Renderer.FLIP_X : Renderer.FLIP_NONE) |
                (Global.random.nextBoolean() ? Renderer.FLIP_Y : Renderer.FLIP_NONE)
        );
    }
}
