package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;

public class HUD extends Menu {
    private static final int HEALTH_SPRITE_X = 10, HEALTH_SPRITE_Y = 11;
    private static final int STAMINA_SPRITE_X = 10, STAMINA_SPRITE_Y = 12;

    private final EntityPlayer player;

    public HUD(EntityPlayer player) {
        this.player = player;
        this.focused = true;
    }

    @Override
    public void render() {
        Renderer.fill(0, Renderer.HEIGHT - 16, Renderer.WIDTH, 16, 000);

        // health
        for (int i = 0; i < this.player.getMaxHealth(); i++) {
            boolean flashing = this.player.health <= 2 && ((Global.frames / 10) % 2) == 0;
            Renderer.render(
                HEALTH_SPRITE_X, HEALTH_SPRITE_Y,
                2 + (i * 8), Renderer.HEIGHT - 16,
                i < this.player.health ?
                    (flashing ?
                        Color.get(311, 511, 533, 555) :
                        Color.get(100, 400, 511, 533)
                    ) :
                    Color.get(100, 111, 222, 333),
                Renderer.FLIP_NONE
            );
        }

        // stamina
        for (int i = 0; i < this.player.getMaxStamina(); i++) {
            boolean flashing = this.player.staminaRechargeDelayTicks > 0 && Global.frames % 3 == 0;
            Renderer.render(
                STAMINA_SPRITE_X, STAMINA_SPRITE_Y,
                2 + (i * 8), Renderer.HEIGHT - 8,
                i < this.player.stamina ?
                    Color.get(110, 331, 441, 551) :
                    (flashing ?
                        Color.get(333, 444, 555, 555) :
                        Color.get(110, 222, 333, 444)),
                Renderer.FLIP_NONE
            );
        }

        // equipped item
        if (player.equipped != null) {
            this.player.equipped.instance.item.renderIcon(
                this.player.equipped.instance,
                Renderer.WIDTH - 100, Renderer.HEIGHT - 12
            );

            int xx = Renderer.WIDTH - 90;
            if (player.equipped.size > 1) {
                String s = Integer.toString(player.equipped.size);
                Font.render(s, xx, Renderer.HEIGHT - 12, 444);
                xx += Font.width(s) + 8;
            }
            Font.render(
                this.player.equipped.instance.item.getName(),
                xx, Renderer.HEIGHT - 12, 555
            );
        }
    }
}
