package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class ItemWheat extends Item {
    public ItemWheat(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "WHEAT";
    }

    @Override
    public int getColor() {
        return Color.get(221, 331, 552, 553);
    }

    @Override
    public int getIconX() {
        return 3;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
