package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TileOre extends TileStone {
    private final int health;
    private final Item item;

    public TileOre(int id, int health, Item item) {
        super(id);
        this.health = health;
        this.item = item;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void bump(Level level, int x, int y, Entity e) {
        super.bump(level, x, y, e);

        if (e instanceof EntityMob) {
            EntityMob mob = (EntityMob) e;
            mob.hurt(1,  mob.getDirection().opposite());
        } else if (e instanceof EntityItem) {
            e.remove();
        }
    }

    @Override
    public boolean collides(Level level, int x, int y, Entity e) {
        return true;
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(Level level, int x, int y) {
        List<ItemStack> result = new ArrayList<>();
        for (int i = Global.random.nextInt(3) + 1; i >= 0; i--) {
            result.add(new ItemStack(new ItemInstance(this.item, 0), 1));
        }
        return result;
    }

    @Override
    public void destroy(Level level, int x, int y, Entity e) {
        super.destroy(level, x, y, e);
        EntitySmashParticle.spawn(level, Level.toCenter(x), Level.toCenter(y), this.item.getColor(), 3, 5);
        level.setTile(x, y, Tile.STONE.id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_OMNI;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public void render(Level level, int x, int y) {
        super.render(level, x, y);

        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    12 + i, 6 + j,
                    Level.toPixel(x) + (i * 8), Level.toPixel(y) + (j * 8),
                    this.item.getColor(), Renderer.FLIP_NONE
                );
            }
        }
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public Tile getBaseTile() {
        return Tile.STONE;
    }
}
