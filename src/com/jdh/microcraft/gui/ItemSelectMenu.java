package com.jdh.microcraft.gui;

import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.util.Control;
import com.jdh.microcraft.util.FMath;

import java.util.List;

public abstract class ItemSelectMenu extends ItemListMenu {
    private static final int WIDTH = 14, HEIGHT = (Renderer.HEIGHT - 32) / 8;

    protected final boolean leftSide;
    protected final Control up, down;
    protected int selectedIndex = 0;

    public ItemSelectMenu(boolean leftSide, Control up, Control down) {
        super(
            leftSide ? 8 : (8 + WIDTH * 8 + 16), 8,
            14, (Renderer.HEIGHT - 32) / 8
        );
        this.leftSide = leftSide;
        this.up = up;
        this.down = down;
    }

    protected void updateOffset()  {
        if (this.selectedIndex < this.getOffset()) {
            this.setOffset(this.selectedIndex);
        }

        if (this.selectedIndex >= this.getOffset() + (HEIGHT - 2)) {
            this.setOffset(this.selectedIndex - (HEIGHT - 2) + 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        List<ItemStack> items = this.getItems();

        if (this.focused) {
            if (this.up.pressedTick() && this.selectedIndex > 0) {
                this.selectedIndex--;
            }

            if (this.down.pressedTick() && this.selectedIndex < (items.size() - 1)) {
                this.selectedIndex++;
            }
        }

        // clamp selection index if its gone out of bounds
        this.selectedIndex = FMath.clamp(this.selectedIndex, 0, items.size() - 1);
        this.updateOffset();
    }

    @Override
    public void render() {
        super.render();

        // render selection indicator
        if (this.selectedIndex != -1) {
            int color = this.focused ? 555 : 333;
            int yy = this.y + (((this.selectedIndex - this.getOffset()) + 1) * 8);
            Font.render(">", this.x, yy, color, Menu.MENU_BG_COLOR);
            Font.render("<", this.x + ((WIDTH - 1) * 8), yy, color, Menu.MENU_BG_COLOR);
        }
    }

    @Override
    public String getItemName(int index) {
        return this.getItems().get(index).instance.item.getName();
    }
}
