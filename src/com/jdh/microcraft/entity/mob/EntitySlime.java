package com.jdh.microcraft.entity.mob;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.ai.AISlime;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

public class EntitySlime extends EntityMob {
    private static final int BASE_SPRITE_X = 16, BASE_SPRITE_Y = 3;

    public boolean jumping;
    private double vx, vy, vz;

    private final boolean jumpTrail;
    private final int color, hitColor, trailColor;
    public final double strength;

    public EntitySlime(Level level, int x, int y, int color, int hitColor,
                       boolean jumpTrail, int trailColor, double strength) {
        super(level, Inventory.NONE);
        this.x = x;
        this.y = y;
        this.color = color;
        this.hitColor = hitColor;
        this.jumpTrail = jumpTrail;
        this.trailColor = trailColor;
        this.strength = strength * Global.game.difficulty;
        this.width = 12;
        this.height = 8;
        this.health = this.getMaxHealth();
        this.stamina = this.getMaxStamina();
        this.ai = new AISlime(this);
    }

    public static EntitySlime createGreenSlime(Level level) {
        return new EntitySlime(
            level,
            0, 0,
            Color.get(010, 020, 151, 353),
            Color.get(010, 020, 353, 454),
            false, 0,
            0.6
        );
    }

    public static EntitySlime createBlueSlime(Level level) {
        return new EntitySlime(
            level,
            0, 0,
            Color.get(001, 002, 114, 225),
            Color.get(001, 002, 335, 445),
            false, 0,
            1.0
        );
    }

    public static EntitySlime createRedSlime(Level level) {
        return new EntitySlime(
            level,
            0, 0,
            Color.get(100, 200, 411, 522),
            Color.get(100, 200, 533, 544),
            true, Color.get(330, 440, 550, 551),
            1.15
        );
    }

    @Override
    public int getHitDamage(Entity e) {
        return (int) (this.strength + Global.random.nextInt((int) (4 * this.strength)));
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);

        if (e instanceof EntityPlayer) {
            this.hit(e);
            e.onHit(this);
        }
    }

    @Override
    public void die() {
        super.die();
        EntitySmashParticle.spawn(this.level, this.x, this.y, this.color, 4, 10);

        for (int i = 1 + Global.random.nextInt(1 + (2 * (int) this.strength)); i > 0; i--) {
            EntityItem.spawn(
                this.level,
                new ItemStack(new ItemInstance(Item.SLIME, 0), 1),
                this.x, this.y
            );
        }

        Global.game.score += 20 * this.strength;
    }

    public void jump(double dx, double dy, double v) {
        double l = FMath.norm(dx, dy);
        this.jumping = true;
        this.vx = 5.0 * (dx / l) * v;
        this.vy = 5.0 * (dy / l) * v;
        this.vz = 5.0 * v;
    }

    public void jump(Direction d, double v) {
        this.jump(
            d.x + (d == Direction.NORTH || d == Direction.SOUTH ? 0.1 : 0.0),
            d.y + (d == Direction.EAST || d == Direction.WEST ? 0.3 : 0.0),
            v
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (this.jumping && this.jumpTrail && Global.random.nextInt(6) == 0) {
            EntitySmashParticle.spawn(this.level, this.x, this.y, this.trailColor, 2, 4);
        }

        if (this.vz > 0.0) {
            this.vx *= 0.97;
            this.vy *= 0.97;
            this.vz -= 0.11;
        } else if (this.vz <= 0.0) {
            this.jumping = false;
            this.vz = 0.0;
            this.vx *= 0.6;
            this.vy *= 0.6;
        }

        if (Math.abs(this.vx) < 0.001) {
            this.vx = 0.0;
        }

        if (Math.abs(this.vy) < 0.001) {
            this.vy = 0.0;
        }

        this.move(
            FMath.tickedDoubleToInt(Global.ticks, this.vx),
            FMath.tickedDoubleToInt(Global.ticks, this.vy)
        );
    }

    @Override
    public int getRenderOffsetX() {
        return -2;
    }

    @Override
    public int getRenderOffsetY() {
        return -8;
    }

    @Override
    public void render() {
        super.render();

        Global.random.setSeed(this.id);
        int color = this.invulnerableTicks > 0 && ((Global.ticks / 4) % 2) == 0 ? this.hitColor : this.color;
        boolean sprite = this.jumping || ((Global.ticks + this.id) / 30) % 2 == 0;
        int sx = sprite ? BASE_SPRITE_X + 2 : BASE_SPRITE_X, sy = BASE_SPRITE_Y;
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    sx + i, sy + j,
                    this.x + this.getRenderOffsetX() + (i * 8),
                    this.y + this.getRenderOffsetY() + (j * 8),
                    color, Renderer.FLIP_NONE
                );
            }
        }
    }

    @Override
    public double getStaminaRechargeRate() {
        return 0.2 * this.strength;
    }

    @Override
    public int getMaxStamina() {
        return (int) (10 * this.strength);
    }

    @Override
    public int getMaxHealth() {
        return (int) (10 * this.strength);
    }

    @Override
    public boolean canSwimIn(Tile tile, int x, int y) {
        return false;
    }
}
