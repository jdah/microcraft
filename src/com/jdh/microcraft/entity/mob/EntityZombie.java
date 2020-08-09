package com.jdh.microcraft.entity.mob;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.ai.AIZombie;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.armor.ItemArmor;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public class EntityZombie extends EntityHumanoid {
    private static final Item[] LOOT_TABLE = {
        Item.GEM, Item.GOLD_INGOT, Item.MITHRIL_INGOT,
        Item.IRON_INGOT, Item.IRON_INGOT, Item.IRON_INGOT, Item.IRON_INGOT,
        Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE,
        Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE, Item.IRON_ORE,
        Item.ROCK, Item.ROCK, Item.ROCK, Item.ROCK, Item.ROCK, Item.ROCK, Item.ROCK
    };

    private final int pantsColor, shirtColor;
    public final double strength;

    public EntityZombie(Level level, int x, int y, double strength) {
        super(level, new Inventory(5));
        this.strength = strength * Global.game.difficulty;
        this.x = x;
        this.y = y;
        this.pantsColor = Color.randomRGB(1, 5);
        this.shirtColor = Color.randomRGB(1, 5);
        this.ai = new AIZombie(this);
        this.health = this.getMaxHealth();
        this.stamina = this.getMaxStamina();
    }

    public static EntityZombie createEasyZombie(Level level) {
        EntityZombie zombie = new EntityZombie(
            level, 0, 0, 0.9
        );
        EntityHumanoid.giveRandomEquipment(zombie, 1, true, true);
        return zombie;
    }

    public static EntityZombie createMediumZombie(Level level) {
        EntityZombie zombie = new EntityZombie(
            level, 0, 0, 1.0
        );
        EntityHumanoid.giveRandomEquipment(zombie, 2, true, true);
        return zombie;
    }


    public static EntityZombie createHardZombie(Level level) {
        EntityZombie zombie = new EntityZombie(
            level, 0, 0, 1.15
        );
        EntityHumanoid.giveRandomEquipment(zombie, 3, true, true);
        return zombie;
    }


    @Override
    protected int getAnimationFrameTicks() {
        return Math.max(10 - ((int) (2.0 / this.strength)), 6);
    }

    @Override
    public void die() {
        super.die();
        EntitySmashParticle.spawn(this.level, this.x, this.y, this.getColor(), 4, 8);

        if (Global.random.nextBoolean()) {
            EntityItem.spawn(
                this.level,
                new ItemStack(new ItemInstance(LOOT_TABLE[Global.random.nextInt(LOOT_TABLE.length)], 0), 1),
                this.x, this.y
            );
        }

        Global.game.score += 30 * this.strength;
    }

    @Override
    public int getHitDamage(Entity e) {
        return (int) (super.getHitDamage(e) * this.strength);
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
    public boolean canPickup(EntityItem e) {
        return (e.stack.instance.item instanceof ItemTool && this.equipped == null) ||
            (e.stack.instance.item instanceof ItemArmor &&
                this.armor[((ItemArmor) e.stack.instance.item).slot] == null);
    }

    @Override
    public void pickup(ItemStack stack) {
        super.pickup(stack);
        if (stack.instance.item instanceof ItemTool && this.equipped == null) {
            this.equipped = stack;
        } else if (stack.instance.item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.instance.item;
            if (this.armor[armor.slot] == null) {
                this.armor[armor.slot] = stack;
            }
        }
    }

    @Override
    public int getColor() {
        return Color.get(000, this.pantsColor, this.shirtColor, 141);
    }

    @Override
    public int getHurtColor() {
        return Color.get(544, Color.add(this.pantsColor, 222), Color.add(this.shirtColor, 222), 555);
    }

    @Override
    public double getStaminaRechargeRate() {
        return 0.15;
    }

    @Override
    public int getMaxStamina() {
        return (int) (10 * this.strength);
    }

    @Override
    public int getMaxHealth() {
        return (int) (10 * this.strength);
    }
}
