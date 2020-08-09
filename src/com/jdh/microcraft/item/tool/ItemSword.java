package com.jdh.microcraft.item.tool;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.Material;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.item.crafting.Recipe;

import java.util.Collection;
import java.util.List;

public class ItemSword extends ItemTool {
    private final int station;

    public ItemSword(int id, int station, Material material) {
        super(id, ItemTool.TYPE_SWORD, material);
        this.station = station;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                this.station,
                new RecipeIngredient(Item.WOOD, 3),
                new RecipeIngredient(this.material.getBase(), 2)
            )
        );
    }

    @Override
    public String getName() {
        return this.material.name + " SWRD";
    }

    @Override
    public int getColor() {
        return material.toolColor;
    }

    @Override
    public int getIconX() {
        return 0;
    }

    @Override
    public int getIconY() {
        return 6;
    }
}
