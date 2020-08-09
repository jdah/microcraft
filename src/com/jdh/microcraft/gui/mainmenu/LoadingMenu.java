package com.jdh.microcraft.gui.mainmenu;

import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gui.Menu;
import com.jdh.microcraft.util.Window;

public class LoadingMenu extends Menu {
    private static final int PROGRESS_WIDTH = 128, PROGRESS_HEIGHT = 4;

    public String text;
    public double progress;

    public LoadingMenu() {
        this.text = "LOADING";
        this.progress = 50.0;
    }

    public void setProgress(double progress) {
        this.setProgress(this.text, progress);
    }

    public void setProgress(String text, double progress) {
        assert(progress >= 0.0 && progress <= 1.0);

        this.text = text;
        this.progress = progress;

        Renderer.clear();
        this.render();
        Window.renderFrame();
    }

    @Override
    public void render() {
        super.render();

        int y = (Renderer.HEIGHT / 2) - 8;
        Font.render(
            this.text,
            (Renderer.WIDTH - Font.width(this.text)) / 2, y,
            555
        );

        Renderer.fill(
            (Renderer.WIDTH - PROGRESS_WIDTH) / 2, y + 12,
            PROGRESS_WIDTH, PROGRESS_HEIGHT,
            333
        );
        Renderer.fill(
            (Renderer.WIDTH - PROGRESS_WIDTH) / 2, y + 12,
            (int) (PROGRESS_WIDTH * this.progress), PROGRESS_HEIGHT,
            151
        );
    }
}
