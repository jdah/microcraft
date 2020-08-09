package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TileBasalt extends TileRock {
    public TileBasalt(int id) {
        super(id);
    }

    @Override
    public List<ItemStack> getDrops(Level level, int x, int y) {
        List<ItemStack> result = new ArrayList<>();

        for (int i = Global.random.nextInt(3); i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(Item.COAL)));
        }

        return result;
    }

    @Override
    public int getHealth() {
        return (int) (super.getHealth() * 2.0);
    }

    @Override
    public int getColor() {
        return Color.get(322, 000, 111, 222);
    }
}
