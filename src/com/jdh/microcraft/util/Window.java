package com.jdh.microcraft.util;

import com.jdh.microcraft.gfx.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;

public class Window {
    // frame buffer image/buffer
    private static BufferedImage image;
    private static int[] buffer;

    // palette image/buffer
    private static BufferedImage paletteImage;
    private static int[] palette;

    private static JFrame frame;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;

    // window size
    public static int width, height;

    // true if window is focused
    public static boolean focused;

    // frame/tick tracking
    private static int frames, ticks, fps, tps;
    private static long lastSecond, lastFrame, frameTime, tickTimeRemaining;

    // if true, window will exit
    private static boolean close;

    // creates a window with the specified width/height
    public static void init(String title, int width, int height) {
        Window.width = width;
        Window.height = height;

        Window.lastSecond = Time.now();
        Window.lastFrame = Time.now();

        // create graphics device compatible buffered image
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        Window.image = graphicsConfiguration.createCompatibleImage(
                com.jdh.microcraft.gfx.Renderer.WIDTH, com.jdh.microcraft.gfx.Renderer.HEIGHT, Transparency.OPAQUE);
        Window.buffer = ((DataBufferInt) Window.image.getRaster().getDataBuffer()).getData();

        // generate palette
        int[] rendererPalette = Renderer.generatePalette();
        Window.paletteImage = graphicsConfiguration.createCompatibleImage(256, 1);
        Window.palette = ((DataBufferInt) Window.paletteImage.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < palette.length; i++) {
            Window.paletteImage.setRGB(i, 0, rendererPalette[i]);
        }

        // open window
        Window.frame = new JFrame(title);
        Window.frame.getContentPane().setPreferredSize(new Dimension(width, height));
        Window.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Window.frame.setResizable(true);
        Window.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Window.close = true;
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                Window.focused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                Window.focused = false;
            }
        });
        Window.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Window.width = Window.canvas.getWidth();
                Window.height = Window.canvas.getHeight();
            }
        });
        Window.frame.setIgnoreRepaint(true);

        Window.canvas = new Canvas(graphicsConfiguration);
        Window.canvas.setPreferredSize(new Dimension(width, height));
        Window.canvas.setSize(width, height);
        Window.canvas.addKeyListener(Keyboard.getListener());
        Window.canvas.setIgnoreRepaint(true);
        Window.frame.add(canvas);
        Window.frame.pack();
        Window.frame.setLocationRelativeTo(null);

        Window.frame.setVisible(true);
        Window.canvas.setVisible(true);

        Window.canvas.createBufferStrategy(2);
        Window.bufferStrategy = Window.canvas.getBufferStrategy();
    }

    public static boolean hasFocus() {
        return Window.frame.isActive();
    }

    public static void close() {
        Window.close = true;
        Window.frame.dispatchEvent(new WindowEvent(Window.frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void renderFrame() {
        // copy renderer pixels to image buffer through palette
        for (int i = 0; i < Renderer.WIDTH * Renderer.HEIGHT; i++) {
            Window.buffer[i] = Window.palette[Renderer.pixels[i]];
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        double rendererAspect = (double) Renderer.WIDTH / (double) Renderer.HEIGHT,
            windowAspect = (double) Window.width / (double) Window.height,
            scaleFactor = windowAspect > rendererAspect ?
                (double) Window.height / (double) Renderer.HEIGHT :
                (double) Window.width / (double) Renderer.WIDTH;

        int rw = (int) (Renderer.WIDTH * scaleFactor), rh = (int) (Renderer.HEIGHT * scaleFactor);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Window.width, Window.height);
        graphics.drawImage(
            image, (Window.width - rw) / 2, (Window.height - rh) / 2,
            rw, rh, null
        );
        graphics.dispose();
        if (!Window.bufferStrategy.contentsLost()) {
            Window.bufferStrategy.show();
        }
    }

    public static void loop(
            Runnable init,
            Runnable destroy,
            Runnable tick,
            Runnable update,
            Runnable render
    ) {
        init.run();

        while (!Window.close) {
            long now = Time.now(), start = now;

            if (now - Window.lastSecond >= Time.NS_PER_SECOND) {
                Window.lastSecond = now;
                Window.fps = Window.frames;
                Window.tps = Window.ticks;
                Window.frames = 0;
                Window.ticks = 0;
                System.out.println(Window.fps + " | " + Window.tps);
            }

            Window.frameTime = now - Window.lastFrame;
            Window.lastFrame = now;

            long tickTime = Window.frameTime + Window.tickTimeRemaining;
            while (tickTime >= Time.NS_PER_TICK) {
                tick.run();
                tickTime -= Time.NS_PER_TICK;
                Window.ticks++;
            }
            Window.tickTimeRemaining = tickTime;

            update.run();
            render.run();

            Window.renderFrame();
            Window.frames++;

            // TODO: make vsync optional
            try {
                long sleepMs = ((16 * Time.NS_PER_MS) - (Time.now() - start)) / Time.NS_PER_MS;
                Thread.sleep(sleepMs < 0 ? 0 : sleepMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        destroy.run();
        System.exit(0);
    }
}
