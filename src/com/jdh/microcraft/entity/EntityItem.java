package com.jdh.microcraft.entity;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.entity.projectile.EntityProjectile;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.Time;

public class EntityItem extends Entity {
    public final ItemStack stack;
    public int timeToLive, timeToPickup;

    private int z;
    private double vx, vy, vz, dx, dy;

    public EntityItem(Level level, ItemStack stack, int x, int y, double vx, double vy, double vz) {
        super(level);
        this.stack = stack;
        this.x = x;
        this.y = y;
        this.z = 4;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.dx = 0;
        this.dy = 0;
        this.timeToLive = Time.TPS * (180 + Global.random.nextInt(60));
        this.timeToPickup = 20;
        this.width = 8;
        this.height = 8;
    }

    @Override
    public boolean collides(Entity e) {
        return e instanceof EntityMob ?
            ((EntityMob) e).canPickup(this) && this.timeToPickup == 0 :
            !(e instanceof EntityItem);
    }

    public static EntityItem spawn(Level level, ItemStack stack, int x, int y, Direction d) {
        Global.random.setSeed(stack.hashCode() + (x * 31) ^ (y * 17));
        EntityItem result = new EntityItem(
            level, stack, x, y,
            d.x * (0.5 + Global.random.nextDouble() * 0.1),
            d.y * (0.5 + Global.random.nextDouble() * 0.1),
            1.0 + (Global.random.nextDouble() * 0.2)
        );
        level.addEntity(result);
        return result;
    }

    public static EntityItem spawn(Level level, ItemStack stack, int x, int y) {
        Global.random.setSeed(stack.hashCode() + (x * 31) ^ (y * 17));
        EntityItem result = new EntityItem(
            level, stack, x, y,
            (Global.random.nextBoolean() ? -1.0 : 1.0) * (0.3 + Global.random.nextDouble() * 0.1),
            (Global.random.nextBoolean() ? -1.0 : 1.0) * (0.3 + Global.random.nextDouble() * 0.1),
            1.0 + (Global.random.nextDouble() * 0.2)
        );
        level.addEntity(result);
        return result;
    }

    public boolean canPickup(Entity e) {
        return this.timeToPickup == 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (--this.timeToLive == 0) {
            this.remove();
        }

        this.timeToPickup = Math.max(this.timeToPickup - 1, 0);

        this.dx += this.vx;
        this.dy += this.vy;

        while (Math.abs(this.dx) >= 1.0) {
            if (!this.moveAxis((int) Math.signum(this.dx), 0)) {
                this.vx *= -0.8;
            }
            this.dx += -Math.signum(this.dx);
        }

        while (Math.abs(this.dy) >= 1.0) {
            if (!this.moveAxis(0, (int) Math.signum(this.dy))) {
                this.vy *= -0.8;
            }
            this.dy += -Math.signum(this.dy);
        }

        this.z += this.vz;

        // bounce
        if (this.z < 0) {
            this.z = 0;
            this.vx *= 0.4;
            this.vy *= 0.4;
            this.vz *= -0.6;
        }

        // gravity
        this.vz -= 0.2;
    }

    @Override
    public void render() {
        this.stack.instance.item.render(this.stack.instance, this.level, this.x, this.y);
    }
}
