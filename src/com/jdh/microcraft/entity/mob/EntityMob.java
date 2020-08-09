package com.jdh.microcraft.entity.mob;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.furniture.EntityFurniture;
import com.jdh.microcraft.entity.particle.EntityTextParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

public abstract class EntityMob extends Entity {
    private static final int HIT_SPRITE_X = 9, HIT_SPRITE_Y = 12;
    private static final int HIT_COLOR = Color.get(222, 533, 544, 555);
    private static final int ACTION_SPRITE_X = 8, ACTION_SPRITE_Y = 11;
    private static final int ACTION_COLOR = Color.get(555, 555, 555, 555);
    private static final int ACTION_TICKS = 6;
    private static final int HIT_TICKS = 4;
    private static final int USE_ITEM_TICKS = 4;

    private static final int STAMINA_RECHARGE_DELAY_TICKS = 40;
    private static final int INVULNERABLE_TICKS = 30;

    protected int actionTicks;
    protected int hitTicks, hitX, hitY;

    protected Item useItem;
    protected int useItemTicks;

    public int health, stamina;
    public int invulnerableTicks;
    public int staminaRechargeDelayTicks;

    public final Inventory inventory;
    public ItemStack equipped;

    public EntityMob(Level level, Inventory inventory) {
        super(level);
        this.health = this.getMaxHealth();
        this.stamina = this.getMaxStamina();
        this.inventory = inventory;
    }

    public void drop(ItemStack stack) {
        assert(this.inventory.contains(stack));

        assert(stack.instance.item.isDroppable());
        EntityItem item = EntityItem.spawn(
            this.level,
            this.removeItem(this.equipped.instance.item, this.equipped.size),
            this.getCenterX(), this.getCenterY(),
            this.getDirection()
        );
        item.timeToPickup = 80;
    }

    public int getFacingTileX() {
        return switch (this.direction) {
            case EAST -> tileX + 1;
            case WEST -> tileX - 1;
            default -> tileX;
        };
    }

    public int getFacingTileY() {
        return switch (this.direction) {
            case NORTH -> tileY - 1;
            case SOUTH -> tileY + 1;
            default -> tileY;
        };
    }

    public ItemStack removeItem(Item item, int count) {
        ItemStack removed = this.inventory.remove(item, count);

        // remove equipped item if equipped stack was removed
        if (this.equipped == removed && count == this.equipped.size) {
            this.equipped = null;
        }

        return removed;
    }

    @Override
    protected void onDirectionChange() {
        this.actionTicks = 0;
        this.useItemTicks = 0;
        this.hitTicks = 0;
    }

    public int getHitDamage(Entity e) {
        return 1;
    }

    public boolean canPickup(EntityItem e) {
        return false;
    }

    public void pickup(ItemStack stack) {

    }

    @Override
    public boolean hit(Entity e) {
        if (!this.swimming && e instanceof EntityMob && this.takeApproxStamina(2)) {
            EntityMob mob = (EntityMob) e;
            mob.hurt(Math.max(this.getHitDamage(e), 1), this);
            return true;
        }

        return e instanceof EntityFurniture;
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);

        if (!e.metadata.remove && e instanceof EntityItem &&
            ((EntityItem) e).canPickup(this) &&
            this.canPickup(((EntityItem) e))) {
            EntityItem item = ((EntityItem) e);
            if (this.inventory.add(item.stack)) {
                this.pickup(item.stack);
                item.remove();
            }
        }
    }

    @Override
    public boolean collides(Entity e) {
        return !(e instanceof EntityMob && this.swimming);
    }

    protected void animateHit(Item used, int x, int y) {
        this.useItem = used;
        this.useItemTicks = USE_ITEM_TICKS;
        this.hitTicks = HIT_TICKS;
        this.hitX = Level.toPixel(x);
        this.hitY = Level.toPixel(y);
    }

    protected void animateAction() {
        this.actionTicks = ACTION_TICKS;
    }

    @Override
    public void render() {
        super.render();

        // used item
        if (this.useItemTicks > 0) {
            ItemInstance instance = new ItemInstance(this.useItem, 0);
            switch (this.direction) {
                case NORTH -> this.useItem.render(
                    instance, this.level,
                    this.x + 2, this.y - 6);
                case SOUTH -> this.useItem.render(
                    instance, this.level,
                    this.x + 2, this.y + this.height);
                case EAST -> this.useItem.render(
                    instance, this.level,
                    this.x + this.width, this.y + 2);
                case WEST -> this.useItem.render(
                    instance, this.level,
                    this.x - 6, this.y + 2);
            }
        }

        if (this.hitTicks > 0) {
            Renderer.render(HIT_SPRITE_X, HIT_SPRITE_Y,
                this.hitX + 0, this.hitY + 0, HIT_COLOR, Renderer.FLIP_Y);
            Renderer.render(HIT_SPRITE_X, HIT_SPRITE_Y,
                this.hitX + 8, this.hitY + 0, HIT_COLOR, Renderer.FLIP_XY);
            Renderer.render(HIT_SPRITE_X, HIT_SPRITE_Y,
                this.hitX + 0, this.hitY + 8, HIT_COLOR, Renderer.FLIP_NONE);
            Renderer.render(HIT_SPRITE_X, HIT_SPRITE_Y,
                this.hitX + 8, this.hitY + 8, HIT_COLOR, Renderer.FLIP_X);
        } else if (this.actionTicks > 0) {
            int px = this.x + this.getRenderOffsetX(), py = this.y + this.getRenderOffsetY();

            switch (this.direction) {
                case NORTH -> {
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y,
                        px, py - 4, ACTION_COLOR, Renderer.FLIP_X);
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y,
                        px + 8, py - 4, ACTION_COLOR, Renderer.FLIP_NONE);
                }
                case SOUTH -> {
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y,
                        px, py + this.height + 1, ACTION_COLOR, Renderer.FLIP_XY);
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y,
                        px + 8, py + this.height + 1, ACTION_COLOR, Renderer.FLIP_Y);
                }
                case EAST -> {
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y + 1,
                        px + this.width, py + 0, ACTION_COLOR, Renderer.FLIP_NONE);
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y + 1,
                        px + this.width, py + 8, ACTION_COLOR, Renderer.FLIP_Y);
                }
                case WEST -> {
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y + 1,
                        px - 4, py + 0, ACTION_COLOR, Renderer.FLIP_X);
                    Renderer.render(ACTION_SPRITE_X, ACTION_SPRITE_Y + 1,
                        px - 4, py + 8, ACTION_COLOR, Renderer.FLIP_XY);
                }
            }
        }
    }

    public void heal(int amount) {
        assert (amount >= 0);
        this.health = Math.min(this.health + amount, this.getMaxHealth());
    }

    // hurt from entity
    public boolean hurt(int amount, Entity e) {
        return this.hurt(
            amount,
            this.moving ?
                this.getDirection().opposite() :
                Direction.get(this.x - e.x, this.y - e.y)
        );
    }

    // hurt with knockback
    public boolean hurt(int amount, Direction direction) {
        if (this.hurt(amount)) {
            this.knockback(4.0, direction);
            return true;
        }
        return false;
    }

    public boolean hurt(int amount) {
        assert (amount >= 0);
        if (this.invulnerableTicks > 0) {
            return false;
        }

        EntityTextParticle.spawn(
            this.level, this.x, this.y,
            Integer.toString(amount),
            amount == 0 ? 114 : 500);

        this.invulnerableTicks = INVULNERABLE_TICKS;
        this.health -= amount;

        if (this.health <= 0) {
            this.die();
            this.remove();
        }

        return true;
    }

    // amount is not exact, it's a multiplier. the true amount is random.
    public boolean takeApproxStamina(int amount) {
        return this.takeStamina(amount - 1 + Global.random.nextInt(1 + amount + (amount / 2)));
    }

    // returns true if stamina was taken
    public boolean takeStamina(int amount) {
        if (this.stamina - amount >= 0) {
            this.stamina -= amount;
            return true;
        }

        return false;
    }

    private void rechargeStamina() {
        if (this.swimming) {
            return;
        }

        double rr = this.getStaminaRechargeRate();
        if (rr >= 1.0) {
            this.stamina += (int) rr;
        } else if (Global.ticks % ((int) (1.0 / this.getStaminaRechargeRate())) == 0) {
            this.stamina++;
        }

        this.stamina = FMath.clamp(this.stamina, 1, this.getMaxStamina());
    }

    @Override
    public void tick() {
        super.tick();

        this.invulnerableTicks = Math.max(this.invulnerableTicks - 1, 0);
        this.hitTicks = Math.max(this.hitTicks - 1, 0);
        this.actionTicks = Math.max(this.actionTicks - 1, 0);
        this.useItemTicks = Math.max(this.useItemTicks - 1, 0);

        this.staminaRechargeDelayTicks = Math.max(this.staminaRechargeDelayTicks - 1, 0);

        if (this.staminaRechargeDelayTicks > 0) {
            this.staminaRechargeDelayTicks--;

            if (this.staminaRechargeDelayTicks == 0) {
                this.rechargeStamina();
            }
        } else if (this.stamina <= 0) {
            this.staminaRechargeDelayTicks = STAMINA_RECHARGE_DELAY_TICKS;
            this.stamina = 0;
        } else if (this.stamina != this.getMaxStamina()) {
            this.rechargeStamina();
        }
    }

    public void die() {

    }

    // per tick
    public abstract double getStaminaRechargeRate();

    public abstract int getMaxStamina();

    public abstract int getMaxHealth();
}
