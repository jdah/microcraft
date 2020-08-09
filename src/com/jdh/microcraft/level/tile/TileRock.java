package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TileRock extends Tile {
    public static int BASE_COLOR = 333;
    public static int COLOR = Color.get(322, BASE_COLOR - 111, BASE_COLOR, BASE_COLOR + 111);

    public TileRock(int id) {
        super(id);
    }

    public int getBaseSpriteX() {
        return 0;
    }

    public int getBaseSpriteY() {
        return 0;
    }

    public int getBorderSpriteX() {
        return 3;
    }

    public int getBorderSpriteY() {
        return 2;
    }

    public int getCornerSpriteX() {
        return 6;
    }

    public int getCornerSpriteY() {
        return 2;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_ALL;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public int getHealth() {
        return 120;
    }

    @Override
    public List<ItemStack> getDrops(Level level, int x, int y) {
        List<ItemStack> result = new ArrayList<>();

        for (int i = Global.random.nextInt(3); i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(Item.ROCK)));
        }

        for (int i = Global.random.nextInt(2); i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(Item.COAL)));
        }

        return result;
    }

    @Override
    public void destroy(Level level, int x, int y, Entity e) {
        super.destroy(level, x, y, e);
        EntitySmashParticle.spawn(level, Level.toCenter(x), Level.toCenter(y), COLOR, 2, 4);
        level.setTile(x, y, Tile.STONE.id);
    }

    @Override
    public boolean collides(Level level, int x, int y, Entity e) {
        return true;
    }

    // c* = connects(x/y/xy)
    // b* = border(x/y)
    // o* = offset(x/y)
    // p* = pixel(x/y)
    private void renderSection(
        boolean cy, boolean cx, boolean cxy,
        int bx, int by,
        int ox, int oy, int px, int py) {
        if (cx && cy) {
            if (cxy) {
                // everything connects
                Renderer.render(
                    this.getBaseSpriteX() + Global.random.nextInt(2),
                    this.getBaseSpriteY() + Global.random.nextInt(2),
                    px, py,
                    this.getColor(),
                    (Global.random.nextBoolean() ? Renderer.FLIP_X : 0) | (Global.random.nextBoolean() ? Renderer.FLIP_Y : 0)
                );
            } else {
                // corner
                Renderer.render(
                    this.getCornerSpriteX() + ox, this.getCornerSpriteY() + oy,
                    px, py,
                    this.getColor(), Renderer.FLIP_NONE
                );
            }
        } else {
            // border
            Renderer.render(
                this.getBorderSpriteX() + bx, this.getBorderSpriteY() + by,
                px, py,
                this.getColor(), Renderer.FLIP_NONE
            );
        }
    }

    @Override
    public void render(Level level, int x, int y) {
        Global.random.setSeed((x * 19) ^ (y * 47));

        boolean
            u = level.getTile(x, y - 1) == this.id,
            d = level.getTile(x, y + 1) == this.id,
            l = level.getTile(x - 1, y) == this.id,
            r = level.getTile(x + 1, y) == this.id,
            ul = level.getTile(x - 1, y - 1) == this.id,
            dl = level.getTile(x - 1, y + 1) == this.id,
            ur = level.getTile(x + 1, y - 1) == this.id,
            dr = level.getTile(x + 1, y + 1) == this.id;

        int px = Level.toPixel(x), py = Level.toPixel(y);
        renderSection(u, l, ul, l ? 1 : 0, u ? 1 : 0, 0, 0, px, py);
        renderSection(u, r, ur, r ? 1 : 2, u ? 1 : 0, 1, 0, px + 8, py);
        renderSection(d, l, dl, l ? 1 : 0, d ? 1 : 2, 0, 1, px, py + 8);
        renderSection(d, r, dr, r ? 1 : 2, d ? 1 : 2, 1, 1, px + 8, py + 8);
    }

    public int getColor() {
        return TileRock.COLOR;
    }
}
