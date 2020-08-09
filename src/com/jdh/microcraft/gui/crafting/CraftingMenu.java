package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.ItemListMenu;
import com.jdh.microcraft.gui.ItemSelectMenu;
import com.jdh.microcraft.gui.Menu;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.Control;
import com.jdh.microcraft.util.ControlHandler;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CraftingMenu extends Menu {
    private class CraftingList extends ItemSelectMenu {
        private final Control craft;

        public CraftingList(Control up, Control down, Control craft) {
            super(true, up, down);
            this.craft = craft;
            this.focused = true;
        }

        public Recipe getSelectedRecipe() {
            if (CraftingMenu.this.recipes.size() == 0) {
                return null;
            }

            return CraftingMenu.this.recipes.get(this.selectedIndex);
        }

        private boolean canCraft(int index) {
            return this.selectedIndex < CraftingMenu.this.recipes.size() &&
                CraftingMenu.this.recipes.get(index)
                    .canMake(CraftingMenu.this.player.inventory, CraftingMenu.this.getStation());
        }

        @Override
        public void tick() {
            super.tick();

            if (this.craft.pressedTick() && this.canCraft(this.selectedIndex)) {
                ItemStack result = this.getSelectedRecipe().make(CraftingMenu.this.player.inventory);
                CraftingMenu.this.player.inventory.add(result);
                CraftingMenu.this.onCraft(result);
            }
        }

        @Override
        public List<ItemStack> getItems() {
            return CraftingMenu.this.items;
        }

        @Override
        public int getItemTextColor(int index) {
            return this.canCraft(index) ? 555 : 333;
        }

        @Override
        public String getName() {
            return CraftingMenu.this.getName();
        }
    }

    private class HaveMenu extends ItemListMenu {
        public HaveMenu() {
            super(
                CraftingMenu.this.craftingList.x + (CraftingMenu.this.craftingList.width + 2) * 8,
                CraftingMenu.this.craftingList.y,
                10, 3
            );
        }

        @Override
        public List<ItemStack> getItems() {
            Recipe recipe = CraftingMenu.this.craftingList.getSelectedRecipe();
            return recipe == null ?
                List.of() :
                List.of(new ItemStack(recipe.result.instance, 1));
        }

        @Override
        public String getItemName(int index) {
            Recipe recipe = CraftingMenu.this.craftingList.getSelectedRecipe();
            assert (recipe != null);

            return Integer.toString(CraftingMenu.this.player.inventory.count(recipe.result.instance.item));
        }

        @Override
        public int getItemTextColor(int index) {
            return 555;
        }

        @Override
        public String getName() {
            return "HAVE";
        }
    }

    private class CostMenu extends ItemListMenu {
        public CostMenu() {
            super(
                CraftingMenu.this.craftingList.x + (CraftingMenu.this.craftingList.width + 2) * 8,
                CraftingMenu.this.craftingList.y + (CraftingMenu.this.haveMenu.height + 1) * 8,
                10, CraftingMenu.this.craftingList.height - CraftingMenu.this.haveMenu.height - 1
            );
        }

        @Override
        public List<ItemStack> getItems() {
            Recipe recipe = CraftingMenu.this.craftingList.getSelectedRecipe();
            return recipe == null ?
                List.of() :
                recipe.ingredients.stream()
                    .map(i -> new ItemStack(new ItemInstance(i.item, 0), 0))
                    .collect(Collectors.toList());
        }

        @Override
        public String getItemName(int index) {
            Recipe recipe = CraftingMenu.this.craftingList.getSelectedRecipe();
            assert (recipe != null);

            ItemStack item = this.getItems().get(index);
            int have = CraftingMenu.this.player.inventory.count(item.instance.item),
                required = recipe.ingredients.stream()
                    .filter(i -> i.item.id == item.instance.item.id)
                    .findFirst().get().count;
            return required + "/" + have;
        }

        @Override
        public int getItemTextColor(int index) {
            return 555;
        }

        @Override
        public String getName() {
            return "COST";
        }
    }

    protected final CraftingList craftingList;
    protected final HaveMenu haveMenu;
    protected final CostMenu costMenu;
    protected final List<Menu> submenus;

    protected final EntityPlayer player;
    protected final List<Recipe> recipes;
    protected final List<ItemStack> items;

    public CraftingMenu(EntityPlayer player) {
        this.player = player;
        this.recipes = Item.STATION_RECIPES.get(this.getStation());
        this.items = this.recipes.stream().map(r -> r.result).collect(Collectors.toList());
        this.craftingList = new CraftingList(ControlHandler.MENU_UP, ControlHandler.MENU_DOWN, ControlHandler.MENU_SELECT);
        this.haveMenu = new HaveMenu();
        this.costMenu = new CostMenu();
        this.submenus = List.of(this.craftingList, this.haveMenu, this.costMenu);
        this.sort();
    }

    private void sort() {
        this.recipes.sort(
            (var a, var b) -> {
                boolean ma = a.canMake(this.player.inventory, this.getStation()),
                    mb = b.canMake(this.player.inventory, this.getStation());
                return ma == mb ? String.CASE_INSENSITIVE_ORDER.compare(
                    a.result.instance.item.getName(),
                    b.result.instance.item.getName()
                ) : (ma ? -1 : 1);
            }
        );

        this.items.clear();
        for (Recipe r : this.recipes) {
            this.items.add(r.result);
        }
    }

    @Override
    public void init() {
        super.init();
        for (Menu m : submenus) {
            m.init();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Menu m : submenus) {
            m.destroy();
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.sort();

        for (Menu m : submenus) {
            m.tick();
        }
    }

    @Override
    public void update() {
        super.update();
        for (Menu m : submenus) {
            m.update();
        }
    }

    @Override
    public void render() {
        super.render();
        for (Menu m : submenus) {
            m.render();
        }
    }

    protected void onCraft(ItemStack stack) {
        Sound.CRAFT.play();
    }

    public abstract int getStation();

    public abstract String getName();
}
