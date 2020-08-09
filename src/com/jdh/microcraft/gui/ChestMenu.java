package com.jdh.microcraft.gui;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityChest;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.util.ControlHandler;

import java.util.List;

public class ChestMenu extends Menu {
    private class ChestPlayerInventoryMenu extends PlayerInventoryMenu {
        public ChestPlayerInventoryMenu(EntityPlayer player) {
            super(player, false);
            this.focused = true;
        }

    }

    private class ChestInventoryMenu extends InventoryMenu {
        public ChestInventoryMenu(EntityChest chest) {
            super(chest.inventory, false);
            this.focused = false;
        }

        @Override
        public String getName() {
            return "CHEST";
        }
    }

    private final ChestPlayerInventoryMenu playerMenu;
    private final ChestInventoryMenu chestMenu;
    private final List<Menu> submenus;

    public ChestMenu(EntityPlayer player, EntityChest chest) {
        this.playerMenu = new ChestPlayerInventoryMenu(player);
        this.chestMenu = new ChestInventoryMenu(chest);
        this.submenus = List.of(this.playerMenu, this.chestMenu);
    }

    @Override
    public void init() {
        super.init();
        for (Menu m : this.submenus) {
            m.init();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Menu m : this.submenus) {
            m.destroy();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (ControlHandler.MENU_LEFT.pressedTick() && !this.playerMenu.focused) {
            this.playerMenu.focused = true;
            this.chestMenu.focused = false;
        }

        if (ControlHandler.MENU_RIGHT.pressedTick() && !this.chestMenu.focused) {
            this.playerMenu.focused = false;
            this.chestMenu.focused = true;
        }

        boolean drop = ControlHandler.DROP.pressedTick();
        if (ControlHandler.MENU_SELECT.pressedTick() || drop) {
            InventoryMenu from = this.playerMenu.focused ? this.playerMenu : this.chestMenu,
                to = from == this.playerMenu ? this.chestMenu : this.playerMenu;

            List<ItemStack> fromItems = from.getItems();
            if (from.selectedIndex >= 0 && from.selectedIndex < fromItems.size()) {
                ItemStack s = fromItems.get(from.selectedIndex);

                if (s.instance.item.isDroppable()) {
                    to.inventory.add(from.inventory.remove(s.instance.item, drop ? 1 : s.size));
                }
            }
        }

        for (Menu m : this.submenus) {
            m.tick();
        }
    }

    @Override
    public void update() {
        super.update();
        for (Menu m : this.submenus) {
            m.update();
        }
    }

    @Override
    public void render() {
        super.render();
        for (Menu m : this.submenus) {
            m.render();
        }
    }
}
