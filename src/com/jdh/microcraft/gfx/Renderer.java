package com.jdh.microcraft.gfx;

import com.jdh.microcraft.util.AABB;
import com.jdh.microcraft.util.FMath;

import java.util.*;

public class Renderer {
    public static final int[] DITHER = new int[]{0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5};

    // renderer flags
    public static final int FLIP_NONE = 0x00, FLIP_X = 0x01, FLIP_Y = 0x02, FLIP_XY = FLIP_X | FLIP_Y;

    // target width/height
    public static final int WIDTH = 256, HEIGHT = 144;

    // screen pixels, each entry in [0..216) referring to palette entry
    // palette is determined by Window
    public static int[] pixels = new int[WIDTH * HEIGHT];

    // global spritesheet
    public static Spritesheet spritesheet = new Spritesheet("/tiles.png", 8);

    // stored camera stack
    private static Stack<Camera> cameraStack = new Stack<>();

    // lights
    private static List<Light> lights = new ArrayList<>();

    // current camera
    public static Camera camera = new Camera();

    // generates color palette, 24-bpp RGB
    public static int[] generatePalette() {
        int[] result = new int[256];

        int i = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255) / 5,
                        gg = (g * 255) / 5,
                        bb = (b * 255) / 5,
                        m = (rr * 30 + gg * 59 + bb * 11) / 100;

                    result[i++] = ((((rr + m) / 2) * 230 / 255 + 10) << 16) |
                        ((((gg + m) / 2) * 230 / 255 + 10) << 8) |
                        ((((bb + m) / 2) * 230 / 255 + 10) << 0);
                }
            }
        }

        return result;
    }

    public static void reset() {
        Renderer.clear();
        Renderer.clearLights();
        Renderer.cameraStack.clear();
        Renderer.camera = new Camera();
    }

    public static void pushCamera() {
        Renderer.cameraStack.push(Renderer.camera);
        Renderer.camera = new Camera();
    }

    public static void popCamera() {
        Renderer.camera = Renderer.cameraStack.pop();
    }

    public static void clearLights() {
        Renderer.lights.clear();
    }

    public static void addLights(Collection<Light> lights) {
        Renderer.lights.addAll(lights);
    }

    public static AABB getAABB() {
        return new AABB(
            Renderer.camera.tx,
            Renderer.camera.ty,
            Renderer.camera.tx + Renderer.WIDTH,
            Renderer.camera.ty + Renderer.HEIGHT
        );
    }

    public static boolean inBounds(int x, int y) {
        int xt = x - Renderer.camera.tx, yt = y - Renderer.camera.ty;
        return xt >= 0 && yt >= 0 && xt < Renderer.WIDTH && yt < Renderer.HEIGHT;
    }

    public static void clear() {
        Arrays.fill(Renderer.pixels, 0);
    }

    public static void fill(int x, int y, int w, int h, int color) {
        int xt = x - Renderer.camera.tx, yt = y - Renderer.camera.ty;

        // check if entirely offscreen
        if (xt + w < 0 || yt + h < 0) {
            return;
        }

        for (int yy = yt; yy < yt + h && yy < Renderer.HEIGHT; yy++) {
            if (yy < 0) {
                continue;
            }

            for (int xx = xt; xx < xt + w && xx < Renderer.WIDTH; xx++) {
                if (xx < 0) {
                    continue;
                }

                Renderer.pixels[yy * Renderer.WIDTH + xx] = Color.map(color);
            }
        }
    }

    public static void render(Sprite sprite, int x, int y, int color) {
        int xt = x - Renderer.camera.tx, yt = y - Renderer.camera.ty;

        if (xt + sprite.width < 0 || yt + sprite.height < 0) {
            return;
        }

        for (int yy = yt, ys = 0; yy < yt + sprite.height && yy < Renderer.HEIGHT; yy++, ys++) {
            if (yy < 0) {
                continue;
            }

            for (int xx = xt, xs = 0; xx < xt + sprite.width && xx < Renderer.WIDTH; xx++, xs++) {
                if (xx < 0) {
                    continue;
                }

                int p = sprite.pixels[ys * sprite.width + xs];
                if (p >= 0) {
                    Renderer.pixels[yy * Renderer.WIDTH + xx] = (color >> (p * 8)) & 0xFF;
                }
            }
        }
    }

    public static void render(int s, int x, int y, int color, int flags) {
        Renderer.render(
            s % Renderer.spritesheet.sizeSprites,
            s / Renderer.spritesheet.sizeSprites,
            x, y, color, flags
        );
    }

    public static void render(int sx, int sy, int x, int y, int color, int flags) {
        int posX = x - camera.tx, posY = y - camera.ty,
            minX = sx * Renderer.spritesheet.size, minY = sy * Renderer.spritesheet.size,
            maxX = minX + Renderer.spritesheet.size, maxY = minY + Renderer.spritesheet.size;

        // sprite will not be shown at all
        if (posX + Renderer.spritesheet.size < 0 || posY + Renderer.spritesheet.size < 0) {
            return;
        }

        for (int ys = minY, yr = posY; ys < maxY && yr < Renderer.HEIGHT; ys++, yr++) {
            if (yr < 0) {
                continue;
            }

            for (int xs = minX, xr = posX; xs < maxX && xr < Renderer.WIDTH; xs++, xr++) {
                if (xr < 0) {
                    continue;
                }

                int p = Renderer.spritesheet.pixels[
                    ((flags & FLIP_Y) == 0 ? ys : (Renderer.spritesheet.size - (ys - minY) - 1 + minY))
                        * Renderer.spritesheet.width +
                        ((flags & FLIP_X) == 0 ? xs : (Renderer.spritesheet.size - (xs - minX) - 1 + minX))];

                if (p >= 0) {
                    Renderer.pixels[yr * Renderer.WIDTH + xr] = (color >> (p * 8)) & 0xFF;
                }
            }
        }
    }

    // lights the current frame stored by the renderer
    public static void light(int d) {
        AABB aabb = Renderer.getAABB();

        // compute (approx.) lights which affect this frame
        Light[] lights = Renderer.lights.stream()
            .filter(l -> {
                int p = l.power * 16;
                return AABB.collide(
                    aabb.minX, aabb.minY, aabb.maxX, aabb.maxY,
                    l.x - p, l.y - p, l.x + p, l.y + p
                );
            }).toArray(Light[]::new);

        int[] oldPixels = Renderer.pixels.clone();
        Arrays.fill(Renderer.pixels, 0);

        int tx = Renderer.camera.tx, ty = Renderer.camera.ty;

        for (int i = 0; i < (Renderer.WIDTH * Renderer.HEIGHT); i++) {
            int x = i % Renderer.WIDTH, y = i / Renderer.WIDTH;

            // compute contribution from each light
            for (Light l : lights) {
                int dx = l.x - (x + tx),
                    dy = l.y - (y + ty),
                    dist = (int) FMath.norm(dx, dy);

                if (dist < (l.power * 4) ||
                    (dist / l.power) <= DITHER[(((Math.abs(dy) % 4) * 4) + (Math.abs(dx) % 4))]) {
                    Renderer.pixels[i] = d != 0 ?
                        Color.map(Color.add(Color.iToRGB(oldPixels[i]), d)) :
                        oldPixels[i];
                    break;
                }
            }
        }
    }
}
