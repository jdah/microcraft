package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.crafting.InventoryCraftingMenu;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.armor.*;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.ControlHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerInventoryMenu extends InventoryMenu {
    private class PlayerArmorMenu extends ItemListMenu {
        public PlayerArmorMenu() {
            super(
                PlayerInventoryMenu.this.x + (PlayerInventoryMenu.this.width + 2) * 8,
                PlayerInventoryMenu.this.y,
                14, 6
            );
        }

        @Override
        public List<ItemStack> getItems() {
            return Arrays.stream(PlayerInventoryMenu.this.player.armor)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        @Override
        public String getItemName(int index) {
            return this.getItems().get(index).instance.item.getName();
        }

        @Override
        public int getItemTextColor(int index) {
            return 444;
        }

        @Override
        public String getName() {
            return "ARMOR";
        }
    }

    private final PlayerArmorMenu armorMenu;
    private final EntityPlayer player;
    private final boolean showArmor;

    public PlayerInventoryMenu(EntityPlayer player, boolean showArmor) {
        super(player.inventory, true);
        this.player = player;
        this.showArmor = showArmor;
        this.focused = true;
        this.armorMenu = new PlayerArmorMenu();

        List<ItemStack> items = this.getItems();
        int selected = items.indexOf(this.player.equipped);
        this.selectedIndex = selected == -1 ? 0 : selected;
        this.updateOffset();
    }

    @Override
    public void tick() {
        super.tick();

        List<ItemStack> items = this.getItems();
        ItemStack selectedItem = this.selectedIndex >= 0 && this.selectedIndex < items.size() ?
            items.get(this.selectedIndex) : null;
        this.player.equipped = selectedItem;

        if (this.showArmor) {
            if (ControlHandler.MENU_EQUIP.pressedTick() &&
                selectedItem != null &&
                selectedItem.instance.item instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) selectedItem.instance.item;

                if (this.player.armor[armor.slot] == selectedItem) {
                    this.player.armor[armor.slot] = null;
                } else {
                    Sound.EQUIP.play();
                    this.player.armor[armor.slot] = selectedItem;
                }
            }

            this.armorMenu.tick();
        }

        if (ControlHandler.DROP.pressedTick() &&
            selectedItem != null &&
            selectedItem.instance.item.isDroppable()) {
            this.player.drop(selectedItem);
        }

        if (ControlHandler.TOGGLE_CRAFTING.pressedTick()) {
            // direct inventory -> crafting transition
            Global.game.setMenu(new InventoryCraftingMenu(this.player));
        }
    }

    @Override
    public void render() {
        super.render();

        if (this.showArmor) {
            this.armorMenu.render();
        }
    }

    @Override
    public void update() {
        if (this.showArmor) {
            this.armorMenu.update();
        }
    }
}
