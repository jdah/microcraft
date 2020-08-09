package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.tool.ItemTool;

public class TileCloudWall extends TileRock {
    public TileCloudWall(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public int getColor() {
        return Color.get(333, 444, 555, 555);
    }
}
