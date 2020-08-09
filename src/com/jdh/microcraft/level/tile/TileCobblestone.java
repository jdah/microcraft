package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;

import java.util.List;

public class TileCobblestone extends TileRock {
    public TileCobblestone(int id) {
        super(id);
    }

    @Override
    public List<ItemStack> getDrops(Level level, int x, int y) {
        return List.of(new ItemStack(Item.ROCK));
    }

    @Override
    public int getBaseSpriteX() {
        return 16;
    }

    @Override
    public int getBaseSpriteY() {
        return 0;
    }

    @Override
    public int getBorderSpriteX() {
        return 18;
    }

    @Override
    public int getBorderSpriteY() {
        return 0;
    }

    @Override
    public int getHealth() {
        return (int) (super.getHealth() * 0.8);
    }

    @Override
    public int getColor() {
        return Tile.ROCK.getColor();
    }
}
