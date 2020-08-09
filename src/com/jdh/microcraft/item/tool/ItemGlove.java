package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Material;

public class ItemGlove extends ItemTool {
    public ItemGlove(int id) {
        super(id, ItemTool.TYPE_OMNI, Material.BASE);
    }

    @Override
    public double getStaminaCostMultiplier() {
        return 0.7;
    }

    @Override
    public boolean isDroppable() {
        return false;
    }

    @Override
    public String getName() {
        return "POW GLOVE";
    }

    @Override
    public int getColor() {
        return Color.get(121, 232, 343, 454);
    }

    @Override
    public int getIconX() {
        return 0;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
