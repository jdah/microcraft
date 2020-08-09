package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class ItemGem extends Item {
    public static final int BASE_COLOR = 533;

    public ItemGem(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "GEM";
    }

    @Override
    public int getColor() {
        return Color.get(Color.add(BASE_COLOR, -211), Color.add(BASE_COLOR, -100), BASE_COLOR, 555);
    }

    @Override
    public int getIconX() {
        return 9;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
