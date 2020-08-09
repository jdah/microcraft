package com.jdh.microcraft.entity.projectile;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.level.Level;

public class EntityArrow extends EntityProjectile {
    public EntityArrow(Level level, Entity shooter, int x, int y, int dx, int dy, double s, int damage) {
        super(level, shooter, x, y, dx, dy, s, damage, 180);
        this.width = 4;
        this.height = 4;
    }

    @Override
    public boolean hit(Entity e) {
        if (e instanceof EntityItem) {
            e.remove();
            return true;
        } else if (!(e instanceof EntityMob)) {
            return false;
        }

        ((EntityMob) e).hurt(this.damage, this);
        this.remove();
        return true;
    }

    @Override
    public int getColor() {
        return Color.get(220, 330, 333, 444);
    }

    @Override
    protected int getHorizontalSpriteX() {
        return 1;
    }

    @Override
    protected int getHorizontalSpriteY() {
        return 8;
    }

    @Override
    protected int getVerticalSpriteX() {
        return 0;
    }

    @Override
    protected int getVerticalSpriteY() {
        return 8;
    }
}
