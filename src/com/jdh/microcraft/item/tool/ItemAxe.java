package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.Material;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.item.crafting.Recipe;

import java.util.Collection;
import java.util.List;

public class ItemAxe extends ItemTool {
    private final int station;

    public ItemAxe(int id, int station, Material material) {
        super(id, ItemTool.TYPE_AXE, material);
        this.station = station;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                this.station,
                new RecipeIngredient(Item.WOOD, 3),
                new RecipeIngredient(this.material.getBase(), 3)
            )
        );
    }

    @Override
    public String getName() {
        return this.material.name + " AXE";
    }

    @Override
    public int getColor() {
        return material.toolColor;
    }

    @Override
    public int getIconX() {
        return 3;
    }

    @Override
    public int getIconY() {
        return 6;
    }
}
