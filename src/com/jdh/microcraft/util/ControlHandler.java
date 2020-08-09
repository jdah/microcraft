package com.jdh.microcraft.util;

public class ControlHandler {
    public static final Control UP = new Control(Keyboard.KEY_W, Keyboard.KEY_UP);
    public static final Control DOWN = new Control(Keyboard.KEY_S, Keyboard.KEY_DOWN);
    public static final Control LEFT = new Control(Keyboard.KEY_A, Keyboard.KEY_LEFT);
    public static final Control RIGHT = new Control(Keyboard.KEY_D, Keyboard.KEY_RIGHT);
    public static final Control HIT = new Control(Keyboard.KEY_SPACE, Keyboard.KEY_ENTER);
    public static final Control INTERACT = new Control(Keyboard.KEY_E);
    public static final Control DROP = new Control(Keyboard.KEY_Q);

    public static final Control TOGGLE_CRAFTING = new Control(Keyboard.KEY_C);

    public static final Control MENU_EQUIP = new Control(Keyboard.KEY_Z);
    public static final Control MENU_UP = new Control(Keyboard.KEY_UP, Keyboard.KEY_W);
    public static final Control MENU_DOWN = new Control(Keyboard.KEY_DOWN, Keyboard.KEY_S);
    public static final Control MENU_LEFT = new Control(Keyboard.KEY_LEFT, Keyboard.KEY_A);
    public static final Control MENU_RIGHT = new Control(Keyboard.KEY_RIGHT, Keyboard.KEY_D);
    public static final Control MENU_SELECT = new Control(Keyboard.KEY_SPACE, Keyboard.KEY_ENTER);
    public static final Control MENU_QUIT = new Control(Keyboard.KEY_ESCAPE);
    public static final Control MENU_INCREASE = new Control(Keyboard.KEY_C);
    public static final Control MENU_DECREASE = new Control(Keyboard.KEY_X);
    public static final Control MENU_RANDOMIZE = new Control(Keyboard.KEY_R);
}
