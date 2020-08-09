package com.jdh.microcraft.gui;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.util.FMath;

import java.util.List;

public abstract class ItemListMenu extends Menu {
    public final int x, y, width, height;
    private int offset = 0;

    public ItemListMenu(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void setOffset(int offset) {
        this.offset = FMath.clamp(offset, 0,this.getItems().size());
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void render() {
        super.render();

        List<ItemStack> items = this.getItems();

        this.renderMenu(this.getName(), this.x, this.y, this.width, this.height);

        // render item list
        for (int i = 0, j = offset; i < this.height - 2 && j < items.size(); i++, j++) {
            int yy = this.y + ((i + 1) * 8);
            ItemStack stack = items.get(j);
            int color = this.getItemTextColor(j);
            stack.instance.item.renderIcon(stack.instance, x + 8, yy);

            int xx = x + 16;
            if (stack.size > 1) {
                String s = Integer.toString(stack.size);
                Font.render(s, xx, yy, Color.add(color, -111));
                xx += Font.width(s) + 8;
            }

            Font.render(this.getItemName(j), xx, yy, color);
        }
    }

    public abstract List<ItemStack> getItems();

    public abstract String getItemName(int index);

    public abstract int getItemTextColor(int index);

    public abstract String getName();
}
