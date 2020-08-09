package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class ItemWood extends Item {
    public static final int BASE_COLOR = 330;

    public ItemWood(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "WOOD";
    }

    @Override
    public int getColor() {
        return Color.get(Color.add(BASE_COLOR, -222), Color.add(BASE_COLOR, -111), BASE_COLOR, BASE_COLOR);
    }

    @Override
    public int getIconX() {
        return 1;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
