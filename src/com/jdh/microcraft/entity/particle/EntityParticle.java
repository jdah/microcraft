package com.jdh.microcraft.entity.particle;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;

public class EntityParticle extends Entity {
    protected int timeToLive;
    protected double vx, vy, dx, dy;
    protected boolean gravity;

    public EntityParticle(Level level, int x, int y) {
        super(level);
        this.x = x;
        this.y = y;
        this.width = 2;
        this.height = 2;
        this.timeToLive = 45 + Global.random.nextInt(60);
        this.vx = (Global.random.nextBoolean() ? -1.0 : 1.0) * (0.2 + (Global.random.nextDouble() * 0.4));
        this.vy = -(0.8 + (Global.random.nextDouble() * 0.5));
        this.gravity = true;
    }

    protected int getRenderX() {
        return (int) (this.x + this.dx);
    }

    protected int getRenderY() {
        return (int) (this.y + this.dy);
    }

    @Override
    public void tick() {
        if (--this.timeToLive == 0) {
            this.remove();
            return;
        }

       this.dx += this.vx;
       this.dy += this.vy;

       if (this.gravity && this.dy >= 8.0) {
           // bounce at bottom
           this.vx *= 0.3;
           this.vy *= (this.vy > 0 ? -1.0 : 1.0) * 0.4;
       } else if (Math.abs(this.vy) < 0.01) {
           // clamp small values
           this.vx = 0.0;
           this.vy = 0.0;
       } else if (this.gravity) {
           // gravity
           this.vy += 0.1;
       }

        if (Math.abs(this.vx) < 0.01) {
            this.vx = 0.0;
        }
    }

    @Override
    public boolean collides(Entity e) {
        return false;
    }

    @Override
    public boolean canSwimIn(Tile tile, int x, int y) {
        return true;
    }
}
