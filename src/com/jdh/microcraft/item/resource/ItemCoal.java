package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class ItemCoal extends Item {
    public ItemCoal(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "COAL";
    }

    @Override
    public int getColor() {
        return Color.get(000, 111, 222, 333);
    }

    @Override
    public int getIconX() {
        return 10;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
