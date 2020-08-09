package com.jdh.microcraft.gui;

import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.util.ControlHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryMenu extends ItemSelectMenu {
    protected final Inventory inventory;

    public InventoryMenu(Inventory inventory, boolean leftSide) {
        super(leftSide, ControlHandler.MENU_UP, ControlHandler.MENU_DOWN);
        this.inventory = inventory;
    }

    @Override
    public List<ItemStack> getItems() {
        // give a sorted list of items with POW GLOVE always on top
        return inventory.stacks.stream()
            .filter(s -> s.instance.item.showInMenu())
            .sorted(Comparator
                .<ItemStack, Boolean>comparing(s -> s.instance.item.id != Item.GLOVE.id)
                .thenComparing(s -> s.instance.item.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public int getItemTextColor(int index) {
        return 555;
    }

    @Override
    public String getName() {
        return "INVENTORY";
    }
}
