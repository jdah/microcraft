package com.jdh.microcraft.item.crafting;

import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    public static final int
        STATION_INVENTORY = 1 << 0,
        STATION_BENCH = 1 << 1,
        STATION_ALL_CRAFTING = STATION_BENCH | STATION_INVENTORY,
        STATION_FURNACE = 1 << 2,
        STATION_OVEN = 1 << 3,
        STATION_ANVIL = 1 << 4;

    public final ItemStack result;
    public final int station;
    public final List<RecipeIngredient> ingredients;

    public Recipe(ItemStack result, int station, RecipeIngredient... ingredients) {
        this.result = result;
        this.station = station;

        // deduplicate ingredients
        this.ingredients = new ArrayList<>();
        for (RecipeIngredient a : ingredients) {
            boolean add = true;

            for (RecipeIngredient b : this.ingredients) {
                if (a.item.id == b.item.id) {
                    b.count += a.count;
                    add = false;
                    break;
                }
            }

            if (add) {
                this.ingredients.add(a);
            }
        }
    }

    public boolean canMake(Inventory inventory, int station) {
        if ((this.station & station) == 0) {
            return false;
        }

        List<RecipeIngredient> requirements = new ArrayList<>(this.ingredients);

        for (ItemStack stack : inventory.stacks) {
            requirements.removeIf(req -> stack.instance.item.id == req.item.id && stack.size >= req.count);
        }

        return requirements.size() == 0;
    }

    public ItemStack make(Inventory inventory) {
        for (RecipeIngredient ingredient : this.ingredients) {
            for (var it = inventory.stacks.iterator(); it.hasNext(); ) {
                ItemStack stack = it.next();
                if (ingredient.item.id == stack.instance.item.id) {
                    assert (stack.size >= ingredient.count);
                    stack.size -= ingredient.count;

                    if (stack.size == 0) {
                        it.remove();
                    }
                }
            }
        }

        return new ItemStack(this.result);
    }

}
