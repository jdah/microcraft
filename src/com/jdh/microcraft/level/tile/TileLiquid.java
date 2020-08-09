package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.level.Level;

public abstract class TileLiquid extends TileGround {
    public TileLiquid(int id) {
        super(id);
    }

    @Override
    public boolean collides(Level level, int x, int y, Entity e) {
        return !e.canSwimIn(this, x, y);
    }
}
