package com.jdh.microcraft.item;

public class ItemStack {
    public ItemInstance instance;
    public int size;

    public ItemStack(ItemStack stack) {
        this(new ItemInstance(stack.instance), stack.size);
    }

    public ItemStack(Item item) {
        this(new ItemInstance(item));
    }

    public ItemStack(Item item, int size) {
        this(new ItemInstance(item), size);
    }

    public ItemStack(ItemInstance instance) {
        this(instance, 1);
    }

    public ItemStack(ItemInstance instance, int size) {
        assert(size > 0);
        assert(size == 1 || instance.item.isStackable());

        this.instance = instance;
        this.size = size;
    }
}
