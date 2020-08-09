package com.jdh.microcraft.entity.projectile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.furniture.EntityFurnace;
import com.jdh.microcraft.entity.furniture.EntityFurniture;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

public abstract class EntityProjectile extends Entity {
    protected final int damage;
    protected final Entity shooter;
    private int timeToLive;
    private double vx, vy;

    public EntityProjectile(Level level, Entity shooter, int x, int y, int dx, int dy, double s, int damage, int ttl) {
        super(level);
        this.shooter = shooter;
        this.damage = damage;
        this.timeToLive = ttl;
        this.x = x;
        this.y = y;

        // compute velocities in direction of dx, dy
        double xx = FMath.sign(dx - this.x),
            yy = FMath.sign(dy - this.y),
            l = FMath.norm(xx, yy);

        xx = (xx / l) * s;
        yy = (yy / l) * s;
        this.vx = Double.isNaN(xx) ? 0 : xx;
        this.vy = Double.isNaN(yy) ? 0 : yy;
    }

    @Override
    public void tick() {
        super.tick();

        this.move(
            FMath.tickedDoubleToInt(Global.ticks, this.vx),
            FMath.tickedDoubleToInt(Global.ticks, this.vy)
        );

        if (--this.timeToLive == 0) {
            this.remove();
        }
    }

    @Override
    public void render() {
        int sx = 0, sy = 0, flip = 0;

        switch (this.direction) {
            case NORTH, SOUTH -> {
                sx = this.getVerticalSpriteX();
                sy = this.getVerticalSpriteY();
                flip = this.direction == Direction.SOUTH ? Renderer.FLIP_Y : 0;
            }
            case EAST, WEST -> {
                sx = this.getHorizontalSpriteX();
                sy = this.getHorizontalSpriteY();
                flip = this.direction == Direction.WEST ? Renderer.FLIP_X : 0;
            }
        }

        Renderer.render(
            sx, sy,
            this.x + this.getRenderOffsetX(), this.y + this.getRenderOffsetY(),
            this.getColor(), flip
        );
    }

    @Override
    public boolean collides(Entity e) {
        return e != this.shooter &&
            !(e instanceof EntityProjectile) &&
            (Math.abs(this.vx) > 0.0 || Math.abs(this.vy) > 0.0);
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);

        if (this.hit(e)) {
            e.onHit(this);
        }
    }

    @Override
    protected boolean moveAxis(int dx, int dy) {
        if (!super.moveAxis(dx, dy)) {
            this.vx = 0.0;
            this.vy = 0.0;
            return false;
        }

        return true;
    }

    @Override
    public boolean canSwimIn(Tile tile, int x, int y) {
        return true;
    }

    @Override
    public abstract boolean hit(Entity e);

    public abstract int getColor();

    protected abstract int getHorizontalSpriteX();

    protected abstract int getHorizontalSpriteY();

    protected abstract int getVerticalSpriteX();

    protected abstract int getVerticalSpriteY();
}
