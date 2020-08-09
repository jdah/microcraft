package com.jdh.microcraft.level.gen;

import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;

public class CloudStairsGenerator extends StairsGenerator {
    public CloudStairsGenerator(Level lower, Level upper) {
        super(lower, upper);
    }

    @Override
    public void generate() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                lower.setTile(
                    i + (lower.width / 2),
                    j + (lower.height / 2),
                    Tile.CLOUD.id
                );
            }
        }

        lower.setTile(lower.width / 2, lower.height / 2, Tile.STAIR_UP.id);
        upper.setTile(upper.width / 2, upper.height / 2, Tile.STAIR_DOWN.id);
    }
}
