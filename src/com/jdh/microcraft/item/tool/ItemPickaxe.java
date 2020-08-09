package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.Material;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.item.crafting.Recipe;

import java.util.Collection;
import java.util.List;

public class ItemPickaxe extends ItemTool {
    private final int station;

    public ItemPickaxe(int id, int station, Material material) {
        super(id, ItemTool.TYPE_PICKAXE, material);
        this.station = station;
    }

    @Override
    public double getStaminaCostMultiplier() {
        return 2.4;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                this.station,
                new RecipeIngredient(Item.WOOD, 3),
                new RecipeIngredient(this.material.getBase(), 4)
            )
        );
    }

    @Override
    public String getName() {
        return this.material.name + " PICK";
    }

    @Override
    public int getColor() {
        return material.toolColor;
    }

    @Override
    public int getIconX() {
        return 1;
    }

    @Override
    public int getIconY() {
        return 6;
    }
}
