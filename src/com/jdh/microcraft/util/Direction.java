package com.jdh.microcraft.util;

import java.util.List;

public enum Direction {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    public static final Direction
        UP = NORTH,
        DOWN = SOUTH,
        RIGHT = EAST,
        LEFT = WEST;

    public static final List<Direction> ALL = List.of(NORTH, SOUTH, EAST, WEST);

    public final int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction get(double x, double y)  {
        if (Math.abs(x) >= Math.abs(y)) {
            return x < 0 ? Direction.WEST : Direction.EAST;
        }

        return y < 0 ? Direction.NORTH : Direction.SOUTH;
    }

    public static Direction get(int x, int y)  {
        return Direction.get((double) x, (double) y);
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
