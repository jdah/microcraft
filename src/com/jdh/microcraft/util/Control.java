package com.jdh.microcraft.util;

public class Control {
    private int[] keys;

    public Control(int... keys) {
        this.keys = keys;
    }

    public boolean down() {
        for (int key : keys) {
            if (Keyboard.keys[key].down) {
                return true;
            }
        }

        return false;
    }

    public boolean pressed() {
        for (int key : keys) {
            if (Keyboard.keys[key].pressed) {
                return true;
            }
        }

        return false;
    }

    public boolean pressedTick() {
        for (int key : keys) {
            if (Keyboard.keys[key].pressedTick) {
                return true;
            }
        }

        return false;
    }
}
