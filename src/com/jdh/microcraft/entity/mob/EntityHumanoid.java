package com.jdh.microcraft.entity.mob;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Light;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.armor.*;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileLiquid;
import com.jdh.microcraft.util.Direction;

public abstract class EntityHumanoid extends EntityMob {
    public static final int BASE_SPRITE_X = 0, BASE_SPRITE_Y = 11;
    public static final int CARRY_SPRITE_X = 0, CARRY_SPRITE_Y = 9;
    private static final int SWIM_SPRITE_X = 9, SWIM_SPRITE_Y = 11;

    private static final int SWIM_COLOR = Color.get(334, 445, 555, 555);

    // current sprite location
    private int spriteX, spriteY;

    // current animation frame ticks and flipped/frame state
    private int animationTicks;
    private boolean animationFlipped, animationXFrame;

    // equipped armor
    public ItemStack[] armor = new ItemStack[ItemArmor.NUM_TYPES];

    public EntityHumanoid(Level level, Inventory inventory) {
        super(level, inventory);
        this.width = 11;
        this.height = 11;
        this.animationTicks = this.getAnimationFrameTicks();
        this.updateAnimationFrame(false);
    }

    // level must be in [1..3]
    public static void giveRandomEquipment(EntityHumanoid entity, int level, boolean armor, boolean weapon) {
        assert (level >= 1 && level <= 3);
        Item[][] armorTable = new Item[][]{
            {Item.IRON_HELMET, Item.IRON_CHESTPLATE, Item.IRON_LEGGINGS, Item.IRON_BOOTS},
            {Item.GOLD_HELMET, Item.GOLD_CHESTPLATE, Item.GOLD_LEGGINGS, Item.GOLD_BOOTS},
            {Item.GOLD_HELMET, Item.GOLD_CHESTPLATE, Item.GOLD_LEGGINGS, Item.GOLD_BOOTS}
        };

        Item[][] weaponTable = new Item[][]{
            {Item.ROCK_SWORD, Item.ROCK_AXE},
            {Item.IRON_SWORD, Item.IRON_AXE},
            {Item.GOLD_SWORD, Item.GEM_SWORD, Item.MITHRIL_SWORD}
        };

        if (armor) {
            for (int i = 0; i < ItemArmor.NUM_TYPES; i++) {
                if (Global.random.nextInt((4 - level) * 7) == 0) {
                    ItemStack item = new ItemStack(new ItemInstance(armorTable[level - 1][i], 0), 1);
                    entity.armor[i] = item;
                    entity.inventory.add(item);
                }
            }
        }

        if (weapon  && Global.random.nextInt((4 - level) * 8) == 0) {
            ItemStack item = new ItemStack(
                new ItemInstance(
                    weaponTable[level - 1][Global.random.nextInt(weaponTable[level - 1].length)],
                    0),
                1
            );
            entity.equipped = item;
            entity.inventory.add(item);
        }
    }

    @Override
    public ItemStack removeItem(Item item, int count) {
        ItemStack removed = super.removeItem(item, count);

        for (int i = 0; i < this.armor.length; i++) {
            if (this.armor[i] == removed) {
                this.armor[i] = null;
            }
        }

        return removed;
    }

    @Override
    public void die() {
        super.die();

        for (ItemStack s : this.inventory.stacks) {
            EntityItem.spawn(this.level, s, this.getCenterX(), this.getCenterY());
        }
    }

    @Override
    public void pickup(ItemStack stack) {
        super.pickup(stack);

        if (stack.instance.item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.instance.item;
            if (this.armor[armor.slot] == null) {
                this.armor[armor.slot] = stack;
            }
        }
    }

    protected int getAnimationFrameTicks() {
        return 10;
    }

    public boolean hurt(int amount, boolean ignoreArmor) {
        if (ignoreArmor) {
            return super.hurt(amount);
        }

        double reduction = 0;
        for (ItemStack s : this.armor) {
            if (s != null) {
                reduction += ((ItemArmor) s.instance.item).getDamageReduction(s.instance);
            }
        }

        reduction = reduction > 0 ? Global.random.nextInt(Math.max((int) (reduction / 2.5), 2)) : 0;
        return super.hurt((int) Math.max(amount - reduction, 0));
    }

    @Override
    public boolean hurt(int amount) {
        return this.hurt(amount, false);
    }

    @Override
    public Light getLight() {
        if (this.equipped != null) {
            int power = this.equipped.instance.item.getLightPower();
            if (power > 0) {
                return new Light(
                    this.getCenterX(),
                    this.getCenterY(),
                    power
                );
            }
        }

        return null;
    }

    @Override
    public int getHitDamage(Entity e) {
        return (this.equipped != null && (this.equipped.instance.item instanceof ItemTool)) ?
            ((ItemTool) this.equipped.instance.item).getDamage(this.equipped.instance) : 1;
    }

    @Override
    public int getRenderOffsetX() {
        return -3;
    }

    @Override
    public int getRenderOffsetY() {
        return -2;
    }

    @Override
    protected void onDirectionChange() {
        super.onDirectionChange();
        this.updateAnimationFrame(false);
    }

    @Override
    public void tick() {
        super.tick();

        // drowning
        if (this.swimming && (Global.ticks % 60) == 0) {
            if (this.stamina == 0) {
                this.hurt(1, true);
            } else {
                this.takeStamina(1);
            }
        }

        if (this.moving) {
            this.animationTicks--;

            if (this.animationTicks == 0) {
                this.updateAnimationFrame(true);
            }
        }

        // pick the correct current animation frame but don't switch
        this.updateAnimationFrame(false);
    }

    protected boolean carrying() {
        return this.equipped != null && this.equipped.instance.item.carry(this);
    }

    public int getBaseSpriteX() {
        return BASE_SPRITE_X;
    }

    public int getBaseSpriteY() {
        return BASE_SPRITE_Y;
    }

    public int getCarrySpriteX() {
        return CARRY_SPRITE_X;
    }

    public int getCarrySpriteY() {
        return CARRY_SPRITE_Y;
    }

    protected int getSpritesheetOffsetX() {
        return this.carrying() ? this.getCarrySpriteX() : this.getBaseSpriteX();
    }

    protected int getSpritesheetOffsetY() {
        return this.carrying() ? this.getCarrySpriteY() : this.getBaseSpriteY();
    }

    protected void updateAnimationFrame(boolean switchFrame) {
        if (switchFrame) {
            this.animationTicks = this.getAnimationFrameTicks();
        }

        this.spriteY = this.getSpritesheetOffsetY();

        switch (this.direction) {
            case NORTH, SOUTH -> {
                if (switchFrame) {
                    this.animationFlipped = !this.animationFlipped;
                }

                this.spriteX = this.getSpritesheetOffsetX() +
                    (this.direction == Direction.NORTH ? 2 : 0);
            }
            case EAST, WEST -> {
                if (switchFrame) {
                    this.animationXFrame = !this.animationXFrame;
                }

                this.animationFlipped = this.direction == Direction.LEFT;
                this.spriteX = this.getSpritesheetOffsetX() +
                    (this.animationXFrame ? 4 : 6);
            }
        }
    }

    private void renderArmor(ItemArmor item) {
        if (this.swimming && item.type != ItemArmor.TYPE_HELMET) {
            return;
        }

        int sx = this.spriteX - this.getCarrySpriteX(),
            sy = this.spriteY - this.getCarrySpriteY();
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    sx + item.getSpriteBaseX() + (this.animationFlipped ? (1 - i) : i),
                    sy + item.getSpriteBaseY() + j,
                    this.x + (i * 8) + this.getRenderOffsetX(),
                    this.y + (j * 8) + this.getRenderOffsetY(),
                    item.getColor(),
                    this.animationFlipped ? Renderer.FLIP_X : Renderer.FLIP_NONE
                );
            }
        }
    }

    @Override
    public void render() {
        super.render();

        // flash with hurt color on hurt
        int color = this.invulnerableTicks > 0 && ((Global.ticks / 4) % 2) == 0 ?
            this.getHurtColor() :
            this.getColor();

        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= (this.swimming ? 0 : 1); j++) {
                Renderer.render(
                    this.spriteX + (this.animationFlipped ? (1 - i) : i), this.spriteY + j,
                    this.x + (i * 8) + this.getRenderOffsetX(),
                    this.y + (j * 8) + this.getRenderOffsetY(),
                    color,
                    this.animationFlipped ? Renderer.FLIP_X : Renderer.FLIP_NONE
                );
            }
        }

        // render armor
        for (ItemStack s : this.armor) {
            if (s != null) {
                this.renderArmor((ItemArmor) s.instance.item);
            }
        }

        if (this.swimming) {
            for (int i = 0; i <= 1; i++) {
                Renderer.render(
                    SWIM_SPRITE_X, SWIM_SPRITE_Y,
                    this.x + (i * 8) + this.getRenderOffsetX(),
                    this.y + 2 + this.getRenderOffsetY(),
                    SWIM_COLOR,
                    i == 0 ? Renderer.FLIP_NONE : Renderer.FLIP_X
                );
            }
        }

        // render carrying item
        if (this.equipped != null && this.equipped.instance.item.carry(this)) {
            this.equipped.instance.item.renderCarry(this.equipped.instance, this.level, this);
        }
    }

    @Override
    public boolean canSwimIn(Tile tile, int x, int y) {
        return tile instanceof TileLiquid;
    }

    public abstract int getColor();

    public abstract int getHurtColor();
}
