package com.jdh.microcraft.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public static final Inventory NONE = new Inventory(0);

    public List<ItemStack> stacks;
    public int maxSize;

    public Inventory(int maxSize) {
        this.stacks = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public boolean contains(Item item, int count) {
        return this.stacks.stream().anyMatch(s -> s.instance.item.id == item.id && s.size >= count);
    }

    public boolean contains(ItemStack stack) {
        return this.stacks.stream().anyMatch(s -> s.equals(stack));
    }

    public ItemStack remove(Item item, int count) {
        assert(this.contains(item, count));
        for (var it = this.stacks.iterator(); it.hasNext(); ) {
            ItemStack s = it.next();
            if (s.instance.item.id == item.id) {
                assert((s.size - count) >= 0);
                if ((s.size - count) == 0) {
                    it.remove();
                    return s;
                } else {
                    s.size -= count;
                    return new ItemStack(new ItemInstance(s.instance), count);
                }
            }
        }

        assert(false);
        return null;
    }

    public boolean add(ItemStack stack) {
        // attempt to stack
        for (ItemStack s : this.stacks) {
            if (s.instance.item.id == stack.instance.item.id &&
                s.instance.item.isStackable()) {
                s.size += stack.size;
                return true;
            }
        }

        // could not stack, try to add to inventory
        if (this.stacks.size() < this.maxSize) {
            this.stacks.add(stack);
            return true;
        }

        return false;
    }

    public ItemStack findById(int id) {
        return this.stacks.stream().filter(s -> s.instance.item.id == id).findFirst().orElse(null);
    }

    public ItemStack findByInstanceId(int id) {
        return this.stacks.stream().filter(s -> s.instance.id == id).findFirst().orElse(null);
    }

    public ItemStack find(Item item) {
        return this.stacks.stream().filter(s -> s.instance.item.id == item.id).findFirst().orElse(null);
    }

    public int count(Item item) {
        return this.stacks.stream().filter(s -> s.instance.item.id == item.id).mapToInt(s -> s.size).sum();
    }
}
