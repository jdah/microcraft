package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.level.Level;

public class TileLava extends TileLiquid {
    public static final int COLOR = Color.get(220, 330, 441, 553),
        TRANSITION_COLOR = Color.get(322,  330, 441, 553);

    public TileLava(int id) {
        super(id);
    }

    @Override
    public void randomTick(Level level, int x, int y) {
        super.randomTick(level, x, y);

        if (Global.random.nextInt(15) == 0) {
            EntitySmashParticle.spawn(
                level,
                Level.toCenter(x), Level.toCenter(y),
                COLOR,
                1, 3
            );
        }
    }

    @Override
    public void step(Level level, int x, int y, Entity e) {
        super.step(level, x, y, e);

        if (e instanceof EntityMob) {
            ((EntityMob) e).hurt(3);
        }
    }

    @Override
    public void bump(Level level, int x, int y, Entity e) {
        super.bump(level, x, y, e);

        if (e instanceof EntityItem) {
            e.remove();
        }
    }

    @Override
    public int getLightPower(Level level, int x, int y) {
        return 3;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public int getColor() {
        return TileLava.COLOR;
    }

    @Override
    public int getTransitionColor() {
        return TileLava.TRANSITION_COLOR;
    }
}
