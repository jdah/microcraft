package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class ItemSlime extends Item {
    public ItemSlime(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "SLIME";
    }

    @Override
    public int getColor() {
        return Color.get(111, 121, 141, 252);
    }

    @Override
    public int getIconX() {
        return 5;
    }

    @Override
    public int getIconY() {
        return 6;
    }
}
