package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.Material;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;

public abstract class ItemTool extends Item {
    public static final int
        TYPE_NONE = 0,
        TYPE_ALL = 0x3F,
        TYPE_SWORD = 1 << 0,
        TYPE_PICKAXE = 1 << 1,
        TYPE_SHOVEL = 1 << 2,
        TYPE_AXE = 1 << 3,
        TYPE_HOE = 1 << 4,
        TYPE_OMNI = 1 << 5;

    public final int type;
    public final Material material;

    public ItemTool(int id, int type, Material material) {
        super(id);
        this.type = type;
        this.material = material;
    }

    public double getStaminaCostMultiplier() {
        return 1.0;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public int getDamage(ItemInstance instance) {
        double effectiveness = 1.0;
        if ((this.type & ItemTool.TYPE_SWORD) != 0) {
            effectiveness *= 1.3;
        } else if ((this.type & ItemTool.TYPE_AXE) != 0) {
            effectiveness *= 1.2;
        } else if ((this.type & ItemTool.TYPE_OMNI) != 0) {
            effectiveness *= 1.0;
        } else {
            effectiveness *= 1.1;
        }

        return 1 + Global.random.nextInt(1 + (int) (effectiveness * this.material.efficiency));
    }

    public boolean canHit(Level level, Tile tile, int x, int y, Entity e) {
        return this.type == TYPE_OMNI || ((tile.getUsableTools() | tile.getIdealTools()) & this.type) != 0;
    }

    public int hit(Level level, Tile tile, int x, int y, Entity e) {
        assert(this.canHit(level, tile,  x, y, e));

        if (this.type == TYPE_OMNI) {
            return 1 + Global.random.nextInt(2);
        }

        boolean ideal = (tile.getIdealTools() & this.type) != 0;
        return (int) (((ideal ? 4 : 1) + Global.random.nextInt(ideal ? 6 : 3)) * this.material.efficiency);
    }

    public boolean canAttack(Entity e) {
        return true;
    }

    public int attack(Entity e) {
        assert(this.canAttack(e));

        if (this.type == TYPE_OMNI) {
            return 1;
        }

        boolean weapon = (this.type & TYPE_SWORD) != 0;
        return (int) (((weapon ? 2 : 1) + Global.random.nextInt(weapon ? 3 : 2)) * this.material.efficiency);
    }
}
