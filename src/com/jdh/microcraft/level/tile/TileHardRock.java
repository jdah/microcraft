package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TileHardRock extends TileRock {
    public TileHardRock(int id) {
        super(id);
    }

    @Override
    public List<ItemStack> getDrops(Level level, int x, int y) {
        List<ItemStack> result = new ArrayList<>();

        for (int i = Global.random.nextInt(2); i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(Item.ROCK)));
        }

        for (int i = Global.random.nextInt(4); i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(Item.COAL)));
        }

        return result;
    }

    @Override
    public int getHealth() {
        return (int) (super.getHealth() * 1.5);
    }

    @Override
    public int getColor() {
        return Color.get(322, 221, 332, 443);
    }
}
