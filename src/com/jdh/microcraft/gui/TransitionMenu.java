package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.util.FMath;

public class TransitionMenu extends Menu {
    private static final int TIME = 10;

    // time to live
    private int ticks;

    public TransitionMenu() {
        this.ticks = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (++this.ticks == TIME) {
            Global.game.setMenu(null);
        }
    }

    @Override
    public void render() {
        super.render();

        for (int y = 0; y < Renderer.HEIGHT / 8; y++) {
            for (int x = 0; x < Renderer.WIDTH / 8; x++) {
                int distance = (int) (
                    FMath.norm(
                        ((double) (x * 8) - (double) (Renderer.WIDTH / 2)) / (Renderer.WIDTH / 2),
                        ((double) (y * 8) - (double) (Renderer.HEIGHT / 2)) / (Renderer.HEIGHT / 2)
                    ) * TIME);

                if (distance > this.ticks) {
                    Renderer.fill(x * 8, y * 8, 8, 8, 000);
                }
            }
        }
    }
}
