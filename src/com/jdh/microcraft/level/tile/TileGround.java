package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.level.Level;

public abstract class TileGround extends Tile {
    private static final int BORDER_SPRITE_X = 0, BORDER_SPRITE_Y = 2;

    public TileGround(int id) {
        super(id);
    }

    protected int getBaseX() {
        return 0;
    }

    protected int getBaseY() {
        return 0;
    }

    protected int getBaseWidth() {
        return 2;
    }

    protected int getBaseHeight() {
        return 2;
    }

    protected int getFlipDirections() {
        return Renderer.FLIP_XY;
    }

    // renders a randomized base tile
    private void renderBase(int px, int py) {
        Renderer.render(
            this.getBaseX() + Global.random.nextInt(this.getBaseWidth()),
            this.getBaseY() + Global.random.nextInt(this.getBaseHeight()),
            px,
            py,
            this.getColor(),
            ((Global.random.nextBoolean() ? 0x01 : 0x00) | (Global.random.nextBoolean() ? 0x02 : 0x00)) &
                this.getFlipDirections()
        );
    }

    @Override
    public void render(Level level, int x, int y) {
        Global.random.setSeed((x * 17) ^ (y * 31) + (this.isAnimated() ? (Global.ticks / 30) : 0));
        int
            tu = level.getTile(x, y - 1),
            td = level.getTile(x, y + 1),
            tl = level.getTile(x - 1, y),
            tr = level.getTile(x + 1, y);

        Tile tb = this.getBaseTile();

        boolean
            u = tu != this.id && tu != tb.id &&
                (!(Tile.TILES[tu] instanceof TileGround) ||
                ((TileGround) Tile.TILES[tu]).getBaseTile().id != tb.id),
            d = td != this.id && td != tb.id &&
                (!(Tile.TILES[td] instanceof TileGround) ||
                ((TileGround) Tile.TILES[td]).getBaseTile().id != tb.id),
            l = tl != this.id && tl != tb.id &&
                (!(Tile.TILES[tl] instanceof TileGround) ||
                ((TileGround) Tile.TILES[tl]).getBaseTile().id != tb.id),
            r = tr != this.id && tr != tb.id &&
                (!(Tile.TILES[tr] instanceof TileGround) ||
                ((TileGround) Tile.TILES[tr]).getBaseTile().id != tb.id);

        int px = Level.toPixel(x), py = Level.toPixel(y);

        if (!u && !l) {
            this.renderBase(px, py);
        } else {
            Renderer.render(
                BORDER_SPRITE_X + (l ? 0 : 1), BORDER_SPRITE_Y + (u ? 0 : 1),
                px, py,
                this.getTransitionColor(), Renderer.FLIP_NONE);
        }

        if (!u && !r) {
            this.renderBase(px + 8, py);
        } else {
            Renderer.render(
                BORDER_SPRITE_X + (r ? 2 : 1), BORDER_SPRITE_Y + (u ? 0 : 1),
                px + 8, py,
                this.getTransitionColor(), Renderer.FLIP_NONE);
        }

        if (!d && !l) {
            this.renderBase(px, py + 8);
        } else {
            Renderer.render(
                BORDER_SPRITE_X + (l ? 0 : 1), BORDER_SPRITE_Y + (d ? 2 : 1),
                px, py + 8,
                this.getTransitionColor(), Renderer.FLIP_NONE);
        }

        if (!d && !r) {
            this.renderBase(px + 8, py + 8);
        } else {
            Renderer.render(
                BORDER_SPRITE_X + (r ? 2 : 1), BORDER_SPRITE_Y + (d ? 2 : 1),
                px + 8, py + 8,
                this.getTransitionColor(), Renderer.FLIP_NONE);
        }
    }

    public Tile getBaseTile() {
        return this;
    }

    public boolean isAnimated() {
        return false;
    }

    public abstract int getColor();

    public abstract int getTransitionColor();
}
