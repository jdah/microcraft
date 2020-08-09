package com.jdh.microcraft;

import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.util.Keyboard;
import com.jdh.microcraft.util.Window;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Optional;

public class Main implements Runnable {
    public static void main(String[] args) {
        System.out.println("CWD is " + Paths.get("").toAbsolutePath().toString());

        // initialize FS
        Optional<URI> testURI = Optional.ofNullable(Main.class.getResource("/tiles.png")).map(url -> {
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                throw new Error(e);
            }
        });

        if (testURI.isPresent() && testURI.get().getScheme().equals("jar")) {
            for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
                if (provider.getScheme().equalsIgnoreCase("jar")) {
                    try {
                        provider.getFileSystem(testURI.get());
                    } catch (FileSystemNotFoundException e) {
                        try {
                            provider.newFileSystem(testURI.get(), Map.of());
                        } catch (IOException ioe) {
                            throw new Error(ioe);
                        }
                    }
                }
            }
        }

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Window.init("MICROCRAFT", screen.width / 2, screen.height / 2);
        new Thread(new Main()).start();
    }

    @Override
    public void run() {
        try {
            Window.loop(
                this::init,
                this::destroy,
                this::tick,
                this::update,
                this::render
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void init() {
        Global.mainMenu = new MainMenuState();
        Global.currentState = Global.mainMenu;
    }

    private void destroy() {

    }

    private void tick() {
        Global.ticks++;
        Keyboard.tick();
        Global.currentState.tick();
    }

    private void update() {
        Keyboard.update();
        Global.currentState.update();
    }

    private void render() {
        Global.frames++;
        Renderer.clear();
        Global.currentState.render();
    }
}
