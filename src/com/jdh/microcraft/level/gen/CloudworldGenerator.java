package com.jdh.microcraft.level.gen;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.util.FMath;

public class CloudworldGenerator extends LevelGenerator {
    private static final int ARENA_RADIUS = 20;

    public CloudworldGenerator(Level level) {
        super(level);
    }

    private void base() {
        for (int y = 0; y < this.level.height; y++) {
            for (int x = 0; x < this.level.width; x++) {
                int distance = (int) FMath.norm(
                    x - (this.level.width / 2),
                    y - (this.level.width / 2));

                int tile = Tile.CLOUD.id;
                if (distance > ARENA_RADIUS) {
                    tile = Tile.SKY.id;
                } else if (distance > ARENA_RADIUS - 2 || Global.random.nextInt(16) == 0) {
                    tile = Tile.CLOUD_WALL.id;
                } else if (Global.random.nextInt(24) == 0) {
                    tile = Tile.MITHRIL_ORE.id;
                }

                level.setTile(x, y, tile);
            }
        }
    }

    @Override
    public void generate() {
        this.setProgress(
            Font.Colors.GREY + "LEVEL " + this.level.depth + ":" +
                Font.Colors.YELLOW + "FORMING...", 0.0);
        this.base();
    }
}
