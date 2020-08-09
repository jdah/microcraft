package com.jdh.microcraft.entity.projectile;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.level.Level;

public class EntityAirBlast extends EntityProjectile {
    public EntityAirBlast(Level level, Entity shooter, int x, int y, int dx, int dy, double s, int damage) {
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
    protected boolean moveAxis(int dx, int dy) {
        if (!super.moveAxis(dx, dy)) {
            this.remove();
            return false;
        }

        return true;
    }

    @Override
    public int getColor() {
        return Color.get(222, 333, 445, 555);
    }

    @Override
    protected int getHorizontalSpriteX() {
        return 14;
    }

    @Override
    protected int getHorizontalSpriteY() {
        return 8;
    }

    @Override
    protected int getVerticalSpriteX() {
        return 14;
    }

    @Override
    protected int getVerticalSpriteY() {
        return 8;
    }
}
