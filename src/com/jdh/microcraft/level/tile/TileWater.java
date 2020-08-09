package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.gfx.Color;

public class TileWater extends TileLiquid {
    public static final int COLOR = Color.get(002, 003, 005, 115),
        TRANSITION_COLOR = Color.get(322,  003, 005, 115);

    public TileWater(int id) {
        super(id);
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public int getColor() {
        return TileWater.COLOR;
    }

    @Override
    public int getTransitionColor() {
        return TileWater.TRANSITION_COLOR;
    }
}
