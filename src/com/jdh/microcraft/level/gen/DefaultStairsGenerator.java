package com.jdh.microcraft.level.gen;

import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileLiquid;
import com.jdh.microcraft.level.tile.TileStair;
import com.jdh.microcraft.util.Direction;

import java.util.Random;

public class DefaultStairsGenerator extends StairsGenerator {
    // maximum number of tries before giving up
    private static final int MAX_TRIES = 4096;

    // generator constraints
    public static final int REQUIRE_ROCKS = 1 << 0;
    public static final int REQUIRE_OPEN = 1 << 1;
    public static final int REQUIRE_NO_LIQUID = 1 << 2;

    private final Random random;
    private final int count, lowerReqs, upperReqs;

    public DefaultStairsGenerator(long seed, Level lower, Level upper, int count, int lowerReqs, int upperReqs) {
        super(lower, upper);
        this.random = new Random(seed);
        this.count = count;
        this.lowerReqs = lowerReqs;
        this.upperReqs = upperReqs;

        assert(this.lower.width == this.upper.width);
        assert(this.lower.height == this.upper.height);
    }

    private boolean checkRequirements(Level level, int x, int y, int reqs) {
        if ((reqs & REQUIRE_ROCKS) != 0) {
            for (Direction d : Direction.ALL) {
                if (level.getTile(x + d.x, y + d.y) != Tile.ROCK.id) {
                    return false;
                }
            }
        }

        if ((reqs & REQUIRE_OPEN) != 0) {
            for (Direction d : Direction.ALL) {
                if (Tile.TILES[level.getTile(x + d.x, y + d.y)].isSolid()) {
                    return false;
                }
            }
        }

        if ((reqs & REQUIRE_NO_LIQUID) != 0) {
            for (Direction d : Direction.ALL) {
                if (Tile.TILES[level.getTile(x + d.x, y + d.y)] instanceof TileLiquid) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void generate() {
        int i = 0, n = 0;
        while (n < count && i < MAX_TRIES) {
            int x = this.random.nextInt(this.lower.width), y = this.random.nextInt(this.lower.height);

            // never allow placement on top of existing stairs
            if (!(Tile.TILES[this.lower.getTile(x, y)] instanceof TileStair) &&
                !(Tile.TILES[this.upper.getTile(x, y)] instanceof TileStair) &&
                this.checkRequirements(lower, x, y, this.lowerReqs) &&
                this.checkRequirements(upper, x, y, this.upperReqs)) {
                // generate here!
                this.lower.setTile(x, y, Tile.STAIR_UP.id);
                this.upper.setTile(x, y, Tile.STAIR_DOWN.id);
                n++;
            }

            i++;
        }

        // place one set in the center of each level, ignoring requirements
        if (i == MAX_TRIES && n == 0) {
            System.err.println(
                "ERROR: could not generate any stairs between levels " +
                this.lower.depth + " and " + this.upper.depth +
                ". Placing in level centers."
            );
            this.lower.setTile(this.lower.width / 2, this.lower.height / 2, Tile.STAIR_UP.id);
            this.upper.setTile(this.upper.width / 2, this.upper.height / 2, Tile.STAIR_DOWN.id);
        } else if (i == MAX_TRIES && n < count) {
            System.err.println(
                "WARNING: could only place " + n + " stairs between levels " +
                    this.lower.depth + " and " + this.upper.depth +
                    ". Tried to place " + this.count + "."
            );
        }
    }
}
