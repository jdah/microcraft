package com.jdh.microcraft.item.crafting;

import com.jdh.microcraft.item.Item;

public class RecipeIngredient {
    public Item item;
    public int count;

    public RecipeIngredient(Item item) {
        this(item, 1);
    }

    public RecipeIngredient(Item item, int count) {
        this.item = item;
        this.count = count;
    }
}
