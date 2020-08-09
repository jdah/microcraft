package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public class TileWheat extends Tile {
    private static final int COLOR = Color.get(221, 141, 552, 553);
    private static final int BASE_SPRITE_X = 10, BASE_SPRITE_Y = 4;

    public TileWheat(int id) {
        super(id);
    }

    public boolean grow(Level level, int x, int y) {
        int stage = level.getData(x, y);

        if (stage < 2) {
            level.setData(x, y, stage + 1);
            return true;
        }

        return false;
    }

    @Override
    public void randomTick(Level level, int x, int y) {
        if (Global.random.nextInt(30) != 0) {
            return;
        }

        this.grow(level, x, y);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_ALL;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_HOE;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        ItemTool tool = (ItemTool) ((EntityMob) e).equipped.instance.item;
        int stage = level.getData(x, y),
            seedChance = (stage + 1) * ((tool.type & ItemTool.TYPE_HOE) != 0 ? 3 : 2),
            wheatChance = switch (stage) {
                case 0 -> 0;
                case 1 -> 3;
                case 2 -> 10;
                default -> -1;
            };

        if (Global.random.nextInt(8) < seedChance) {
            for (int i = Global.random.nextInt(2); i >= 0; i--) {
                EntityItem.spawn(
                    level, new ItemStack(new ItemInstance(Item.SEED)),
                    Level.toPixel(x), Level.toPixel(y)
                );
            }
        }

        if (Global.random.nextInt(8) < wheatChance) {
            EntityItem.spawn(
                level, new ItemStack(new ItemInstance(Item.WHEAT)),
                Level.toPixel(x), Level.toPixel(y)
            );
        }

        level.setTile(x, y, Tile.FARMLAND.id);

        return true;
    }

    @Override
    public void render(Level level, int x, int y) {
        Global.random.setSeed((x * 19) ^ (y * 117));
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    BASE_SPRITE_X + level.getData(x, y),
                    BASE_SPRITE_Y,
                    Level.toPixel(x) + (i * 8), Level.toPixel(y) + (j * 8),
                    TileWheat.COLOR,
                    Global.random.nextBoolean() ? Renderer.FLIP_X : Renderer.FLIP_NONE
                );
            }
        }
    }
}
