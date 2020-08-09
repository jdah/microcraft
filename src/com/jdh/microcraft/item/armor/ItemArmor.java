package com.jdh.microcraft.item.armor;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.Material;

public abstract class ItemArmor extends Item {
    public static final int NUM_TYPES = 4;

    public static final int
        TYPE_NONE = 0,
        TYPE_HELMET = 1 << 0,
        TYPE_CHESTPLATE = 1 << 1,
        TYPE_LEGGINGS = 1 << 2,
        TYPE_BOOTS = 1 << 3;

    public static final Class[] CLASSES = new Class[]{
        ItemHelmet.class,
        ItemChestplate.class,
        ItemLeggings.class,
        ItemBoots.class
    };

    public final int type, slot;
    public final Material material;

    public ItemArmor(int id, int slot, int type, Material material) {
        super(id);
        this.slot = slot;
        this.type = type;
        this.material = material;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public double getDamageReduction(ItemInstance instance) {
        return this.material.efficiency *
            switch (this.type) {
                case TYPE_HELMET -> 0.55;
                case TYPE_CHESTPLATE -> 1.0;
                case TYPE_LEGGINGS -> 0.8;
                case TYPE_BOOTS -> 0.65;
                default -> throw new IllegalStateException("Unexpected value: " + this.type);
            };
    }

    public abstract int getSpriteBaseX();

    public abstract int getSpriteBaseY();
}
