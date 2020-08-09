package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;

public class TileDaisy extends TileFlower {

    public TileDaisy(int id) {
        super(id);
    }

    @Override
    public Item getDrop() {
        return Item.DAISY;
    }

    @Override
    public int getFlowerColor() {
        return Color.get(222, 444, 442, 553);
    }
}
