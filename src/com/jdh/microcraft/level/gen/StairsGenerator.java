package com.jdh.microcraft.level.gen;

import com.jdh.microcraft.level.Level;

public abstract class StairsGenerator {
    public final Level upper, lower;

    public StairsGenerator(Level lower, Level upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public abstract void generate();
}
