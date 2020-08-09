package com.jdh.microcraft.gfx;

import com.jdh.microcraft.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Spritesheet {
    // spritesheet size in pixels
    public int width, height;

    // spritesheet sprite size
    public int size;

    // spritesheet size in sprites
    public int sizeSprites;

    // spritesheet pixels, mapped into [0..3] space
    public int[] pixels;

    public Spritesheet(String path, int size) {
        this.size = size;

        BufferedImage image;

        try {
            image = ImageIO.read(Main.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new Error(e);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[this.width * this.height];

        assert(this.width == this.height);
        this.sizeSprites = this.width / this.size;

        int[] imagePixels = image.getRGB(
            0, 0, image.getWidth(), image.getHeight(),
            null, 0, image.getWidth());

        for (int i = 0; i < this.width * this.height; i++) {
            this.pixels[i] = ((imagePixels[i] >> 24) & 0xFF) != 0xFF ? -1 : (imagePixels[i] & 0xFF) / 64;
        }
    }
}
