package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.entity.*;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.entity.particle.EntityTextParticle;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

import java.util.List;

public abstract class Tile {
    public static final int TILE_MAX = 256;
    public static final Tile[] TILES = new Tile[TILE_MAX];

    public static final TileAir AIR = new TileAir(0);
    public static final TileGrass GRASS = new TileGrass(1);
    public static final TileSand SAND = new TileSand(2);
    public static final TileWater WATER = new TileWater(3);
    public static final TileRock ROCK = new TileRock(4);
    public static final TileTree TREE = new TileTree(5);
    public static final TileDaisy DAISY = new TileDaisy(6);
    public static final TilePoppy POPPY = new TilePoppy(7);
    public static final TileTallGrass TALL_GRASS = new TileTallGrass(8);
    public static final TileCactus CACTUS = new TileCactus(9);
    public static final TileStone STONE = new TileStone(10);
    public static final TileDirt DIRT = new TileDirt(11);
    public static final TileFarmland FARMLAND = new TileFarmland(12);
    public static final TileWheat WHEAT = new TileWheat(13);
    public static final TileSapling SAPLING = new TileSapling(14);
    public static final TileStair STAIR_UP = new TileStair(15, true);
    public static final TileStair STAIR_DOWN = new TileStair(16, false);
    public static final TileOre IRON_ORE = new TileOre(17, 200, Item.IRON_ORE);
    public static final TileOre GOLD_ORE = new TileOre(18, 300, Item.GOLD_ORE);
    public static final TileOre GEM_ORE = new TileOre(19, 300, Item.GEM);
    public static final TileOre MITHRIL_ORE = new TileOre(20, 500, Item.MITHRIL_ORE);
    public static final TileHardRock HARD_ROCK = new TileHardRock(21);
    public static final TileBasalt BASALT = new TileBasalt(22);
    public static final TileCobblestone COBBLESTONE = new TileCobblestone(23);
    public static final TileLava LAVA = new TileLava(24);
    public static final TileCloud CLOUD = new TileCloud(25);
    public static final TileCloudWall CLOUD_WALL = new TileCloudWall(26);
    public static final TileSky SKY = new TileSky(27);

    public final int id;

    public Tile(int id) {
        this.id = id;
        assert(Tile.TILES[this.id] == null);
        Tile.TILES[this.id] = this;
    }

    public int getLightPower(Level level, int x, int y) {
        return 0;
    }

    public int getCollisionWidth() {
        return 16;
    }

    public int getCollisionHeight() {
        return 16;
    }

    public int getCollisionOffsetX() {
        return 0;
    }

    public int getCollisionOffsetY() {
        return 0;
    }

    public boolean isDestructible() {
        return false;
    }

    public boolean isSolid() {
        return false;
    }

    public int getHealth() {
        return 0;
    }

    public List<ItemStack> getDrops(Level level, int x, int y) {
        return List.of();
    }

    public void destroy(Level level, int x, int y, Entity e) {

    }

    public void step(Level level, int x, int y, Entity e) {

    }

    public void bump(Level level, int x, int y, Entity e) {

    }

    public int getUsableTools() {
        return ItemTool.TYPE_NONE;
    }

    public int getIdealTools() {
        return ItemTool.TYPE_NONE;
    }

    public boolean hit(Level level, int x, int y, Entity e) {
        if (!(e instanceof EntityMob)) {
            return false;
        }

        EntityMob mob = ((EntityMob) e);
        if (!(mob.equipped.instance.item instanceof ItemTool)) {
            return false;
        }

        ItemTool tool = ((ItemTool) mob.equipped.instance.item);
        if (!tool.canHit(level, this, x, y, e)) {
            return false;
        }

        if (this.isDestructible()) {
            int d = tool.hit(level, this, x, y, e);
            int damage = level.getData(x, y) + d;

            if (damage > this.getHealth()) {
                this.destroy(level, x, y, e);
                for (ItemStack stack : this.getDrops(level, x, y)) {
                    EntityItem.spawn(
                        level, stack,
                        Level.toCenter(x), Level.toCenter(y)
                    );
                }
            } else {
                level.setData(x, y, damage + d);
            }

            EntityTextParticle.spawn(
                level,
                Level.toCenter(x), Level.toCenter(y),
                Integer.toString(d), 311
            );
        }

        return true;
    }

    public boolean interact(Level level, int x, int y, Entity e) {
        return false;
    }

    public void randomTick(Level level, int x, int y) {

    }

    public boolean collides(Level level, int x, int y, Entity e) {
        return false;
    }

    public abstract void render(Level level, int x, int y);
}
