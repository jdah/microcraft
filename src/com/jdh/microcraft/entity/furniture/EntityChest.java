package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.ChestMenu;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;
import java.util.List;

public class EntityChest extends EntityFurniture {
    public Inventory inventory = new Inventory(512);

    public EntityChest(Level level) {
        super(level, Item.CHEST, 15, 13);
    }

    @Override
    public boolean hit(Entity e) {
        if (super.hit(e)) {
            for (ItemStack s : this.inventory.stacks) {
                EntityItem.spawn(this.level, s, Level.toCenter(e.tileX), Level.toCenter(e.tileY));
            }

            return true;
        }

        return false;
    }
    @Override
    protected List<ItemStack> getDrops() {
        List<ItemStack> chest_items = this.inventory.stacks;
        chest_items.addAll(super.getDrops());
        return chest_items;
    }

    @Override
    public boolean interact(Entity e) {
        if (!(e instanceof EntityPlayer)) {
            return false;
        }

        Global.game.setMenu(new ChestMenu((EntityPlayer) e, this));
        return true;
    }
}
