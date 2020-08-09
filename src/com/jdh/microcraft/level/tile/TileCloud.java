package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.gfx.Color;

public class TileCloud extends TileGround {
    public TileCloud(int id) {
        super(id);
    }

    @Override
    public int getColor() {
        return Color.get(222, 333, 444, 555);
    }

    @Override
    public int getTransitionColor() {
        return Color.get(222, 333, 444, 555);
    }
}
