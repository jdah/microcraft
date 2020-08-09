package com.jdh.microcraft.level.gen;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileOre;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

import java.util.List;

public class UnderworldGenerator extends LevelGenerator {
    public static class OreGenerationProperties {
        public final TileOre tile;
        public final int rarity;

        public OreGenerationProperties(TileOre tile, int rarity) {
            this.tile = tile;
            this.rarity = rarity;
        }
    }

    private final Tile ground, dirt, rock;
    private final List<OreGenerationProperties> ores;
    private final int waterChance, lavaChance;

    public UnderworldGenerator(Level level,
                               Tile ground, Tile dirt, Tile rock,
                               List<OreGenerationProperties> ores,
                               int waterChance, int lavaChance) {
        super(level);
        this.ground = ground;
        this.dirt = dirt;
        this.rock = rock;
        this.ores = ores;
        this.waterChance = waterChance;
        this.lavaChance = lavaChance;
    }

    private void base() {
        double ob = this.random.nextDouble() * 1024.0f,
            or = this.random.nextDouble() * 1024.0f,
            ot = this.random.nextDouble() * 1024.0f;

        for (int y = 0; y < this.level.height; y++) {
            for (int x = 0; x < this.level.width; x++) {
                if (x == 0 ||
                    y == 0 ||
                    x == (this.level.width - 1) ||
                    y == (this.level.height - 1)) {
                    // borders
                    level.setTile(x, y, this.rock.id);
                    continue;
                }

                double nb = FMath.safepow(this.noise.eval(x / 8.0f, y / 8.0f, ob), 1.1),
                    nr = this.noise.eval(x / 8.0f, y / 8.0f, or),
                    nt = FMath.safepow(this.noise.eval(x / 16.0f, y / 16.0f, ot), 1.3);

                // compute height, make higher closer to edges
                double dist = FMath.norm(
                    Math.abs(x - (this.level.width / 2.0)) / (this.level.width / 2.0),
                    Math.abs(y - (this.level.height / 2.0)) / (this.level.height / 2.0)),
                    h = nb + (nr * 0.5) + (dist > (1.0 - (32 * (1.0 / this.level.width))) ? 0.4 : 0.0);

                int tile;
                if (h > -0.05) {
                    tile = this.rock.id;
                } else if (nt > 0.2) {
                    tile = this.dirt.id;
                } else {
                    tile = this.ground.id;
                }

                level.setTile(x, y, tile);
            }
        }
    }

    // place ores
    public void ore() {
        for (int y = 0; y < this.level.height; y++) {
            for (int x = 0; x < this.level.width; x++) {
                for (OreGenerationProperties ore : this.ores) {
                    if (Global.random.nextInt(ore.rarity) == 0) {
                        level.setTile(x, y, ore.tile.id);
                        break;
                    }
                }
            }
        }
    }

    private boolean isValidForPool(Tile poolTile, int t) {
        return t == this.ground.id ||
            t == this.rock.id ||
            t == this.dirt.id ||
            t == poolTile.id;
    }

    // liquid pools
    public void pools(Tile tile, int chance) {
        int i = 0,
            n = 0,
            count = chance * ((this.level.width * this.level.height) / (128 * 128));
        while (i < count && n < count * 64) {
            n++;

            int x = Global.random.nextInt(this.level.width),
                y = Global.random.nextInt(this.level.height);

            if (!this.isValidForPool(tile, this.level.getTile(x, y))) {
                continue;
            }

            i++;

            int s = Global.random.nextInt(3),
                l = s + Global.random.nextInt(3),
                w = s + Global.random.nextInt(3);

            for (int yy = x; yy < x + l; yy++) {
                for (int xx = y; xx < x + w; xx++) {
                    // check valid
                    boolean valid = true;
                    for (Direction d : Direction.ALL) {
                        int t = this.level.getTile(xx + d.x, yy + d.y);
                        if (!this.isValidForPool(tile, t)) {
                            valid = false;
                            break;
                        }
                    }

                    if (!valid) {
                        continue;
                    }

                    this.level.setTile(xx, yy, tile.id);
                }
            }
        }
    }

    @Override
    public void generate() {
        this.setProgress(
            Font.Colors.GREY + "LEVEL " + this.level.depth + ":" +
                Font.Colors.YELLOW + "FORMING...", 0.0);
        this.base();
        this.setProgress(
            Font.Colors.GREY + "LEVEL " + this.level.depth + ":" +
                Font.Colors.YELLOW + "ENRICHING...", 0.33);
        this.ore();
        this.setProgress(
            Font.Colors.GREY + "LEVEL " + this.level.depth + ":" +
                Font.Colors.YELLOW + "LIQUID-IFYING...", 0.66);
        this.pools(Tile.WATER, this.waterChance);
        this.pools(Tile.LAVA, this.lavaChance);
    }
}
