package com.jdh.microcraft.item;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.resource.ItemIngot;
import com.jdh.microcraft.item.resource.ItemOre;

public class Metal {
    public static final Metal GOLD =
        new Metal("GOLD",4, 5,
            441, Color.get(221, 331, 441, 553));
    public static final Metal IRON =
        new Metal("IRON", 6, 7,
            444, Color.get(222, 333, 444, 555));
    public static final Metal MITHRIL =
        new Metal("MITH", 51, 52,
            334, Color.get(222, 224, 335, 555));

    public final String name;
    public final int oreId, ingotId;
    public final int baseColor, color;

    public Metal(String name, int oreId, int ingotId, int baseColor, int color) {
        this.name = name;
        this.baseColor = baseColor;
        this.color = color;
        this.oreId = oreId;
        this.ingotId = ingotId;
    }

    public ItemOre getOre() {
        return (ItemOre) Item.ITEMS[this.oreId];
    }

    public ItemIngot getIngot() {
        return (ItemIngot) Item.ITEMS[this.ingotId];
    }
}
