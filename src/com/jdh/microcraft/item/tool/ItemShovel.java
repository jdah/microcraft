package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.Material;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.item.crafting.Recipe;

import java.util.Collection;
import java.util.List;

public class ItemShovel extends ItemTool {
    private final int station;

    public ItemShovel(int id, int station, Material material) {
        super(id, ItemTool.TYPE_SHOVEL, material);
        this.station = station;
    }

    @Override
    public double getStaminaCostMultiplier() {
        return 1.2;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                this.station,
                new RecipeIngredient(Item.WOOD, 3),
                new RecipeIngredient(this.material.getBase(), 1)
            )
        );
    }

    @Override
    public String getName() {
        return this.material.name + " SHVL";
    }

    @Override
    public int getColor() {
        return material.toolColor;
    }

    @Override
    public int getIconX() {
        return 2;
    }

    @Override
    public int getIconY() {
        return 6;
    }
}
