package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class TilePoppy extends TileFlower {

    public TilePoppy(int id) {
        super(id);
    }

    @Override
    public Item getDrop() {
        return Item.POPPY;
    }

    @Override
    public int getFlowerColor() {
        return Color.get(222, 422, 441, 553);
    }
}
