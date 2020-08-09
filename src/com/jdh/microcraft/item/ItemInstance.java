package com.jdh.microcraft.item;

import com.jdh.microcraft.Global;

public class ItemInstance {
    public Item item;
    public int data, id;

    public ItemInstance(ItemInstance instance) {
        this(instance.item, instance.data);
    }

    public ItemInstance(Item item) {
        this(item, 0);
    }

    public ItemInstance(Item item, int data) {
        this.item = item;
        this.data = data;
        this.id = Global.game.getNextItemInstanceId();
    }
}
