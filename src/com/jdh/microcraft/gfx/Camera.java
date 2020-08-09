package com.jdh.microcraft.gfx;

import com.jdh.microcraft.util.FMath;

public class Camera {
    // translation
    public int tx = 0, ty = 0;

    public void centerOn(int x, int y, int minX, int minY, int maxX, int maxY) {
        this.tx = FMath.clamp(x - (Renderer.WIDTH / 2), minX, maxX - Renderer.WIDTH);
        this.ty = FMath.clamp(y - (Renderer.HEIGHT / 2), minY, maxY - Renderer.HEIGHT);
    }
}
