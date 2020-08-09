package com.jdh.microcraft.entity;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.ai.AI;
import com.jdh.microcraft.gfx.Light;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.LevelEntityMetadata;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileLiquid;
import com.jdh.microcraft.level.tile.TileStair;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

import java.util.List;

public class Entity {
    // current location
    public int x, y;

    // size
    public int width, height;

    // level this entity belongs to
    public Level level;

    // set to true on ticks where entity moves
    public boolean moving;

    // set to true if the entity is in a liquid
    public boolean swimming;

    // true if the entity is currently on stairs (onStairs) or has entered stairs this tick (onStairsTick)
    public boolean onStairs;

    // metadata about this entity, tracked by the current level
    public final LevelEntityMetadata metadata;

    // current tile locations, managed by Level
    // location of CENTER of entity
    public int tileX = -1, tileY = -1;

    // unique entity id
    public final int id;

    // entity AI, optional
    public AI ai;

    // current direction and direction last tick
    protected Direction direction = Direction.DOWN;
    private Direction lastDirection;

    // positions at the LAST tick
    private int lastX, lastY;

    // knockback velocity
    private double kx, ky;

    public Entity(Level level) {
        this.level = level;
        this.id = Global.game.getNextEntityId();
        this.width = 6;
        this.height = 6;
        this.metadata = new LevelEntityMetadata(this);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Light getLight() {
        return null;
    }

    public void remove() {
        this.metadata.remove = true;
    }

    // schedules this entity to be moved to another level on the next tick
    public void moveLevel(int depth, int x, int y) {
        this.metadata.moveLevel(depth, x,  y);
    }

    // called when this entity moves between levels
    public void onDepthChange(int prevDepth) {

    }

    public void knockback(double strength, Direction d) {
        this.knockback(strength, d.x, d.y);
    }

    public void knockback(double strength, int x, int y) {
        this.kx = Math.signum(x) * strength;
        this.ky = Math.signum(y) * strength;
    }

    public int getRenderOffsetX() {
        return 0;
    }

    public int getRenderOffsetY() {
        return 0;
    }

    protected void onDirectionChange() {

    }

    public void tick() {
        this.moving = this.lastX != this.x || this.lastY != this.y;

        int dx = this.x - this.lastX, dy = this.y - this.lastY;
        if (dx != 0 || dy != 0) {
            this.direction = Direction.get(dx, dy);
        }

        if (this.direction != this.lastDirection) {
            this.onDirectionChange();
        }

        this.lastDirection = this.direction;

        this.lastX = this.x;
        this.lastY = this.y;
        this.swimming = Tile.TILES[this.level.getTile(this.tileX, this.tileY)] instanceof TileLiquid;
        this.onStairs = Tile.TILES[this.level.getTile(this.tileX, this.tileY)] instanceof TileStair;

        // apply knockback
        this.move(
            FMath.tickedDoubleToInt(Global.ticks, this.kx),
            FMath.tickedDoubleToInt(Global.ticks, this.ky)
        );
        this.kx *= 0.85;
        this.ky *= 0.85;

        if (this.ai != null) {
            this.ai.tick();
        }
    }

    public void update() {
        if (this.ai != null) {
            this.ai.update();
        }
    }

    public void render() {

    }

    public boolean collides(Entity e) {
        return true;
    }

    public void collide(Entity e) {

    }

    // called if THIS entity hits E
    public boolean hit(Entity e)  {
        return false;
    }

    // called when E hits THIS entity
    public void onHit(Entity e) {

    }

    public boolean interact(Entity e) {
        return false;
    }

    public void move(int dx, int dy) {
        this.moveAxis(dx, 0);
        this.moveAxis(0, dy);
    }

    // moves along a single axis
    protected boolean moveAxis(int dx, int dy) {
        assert(dx == 0 || dy == 0);
        int ox = this.x, oy = this.y;
        this.x += dx;
        this.y += dy;

        // deny any movement that would move this entity off of the map
        if (this.x < 0 || this.y < 0 ||
            (this.x + this.width) >= Level.toPixel(this.level.width) ||
            (this.y + this.height >= Level.toPixel(this.level.height))) {
            this.x = ox;
            this.y = oy;
            return false;
        }

        List<Entity> entities = level.getEntityCollisions(this);
        for (Entity e : entities) {
            if (e != this && this.collides(e) && e.collides(this)) {
                this.collide(e);
                e.collide(this);

                // deny movement if it is TOWARDS the colliding entity
                if ((dx != 0 && FMath.sameSign(dx, e.x - this.x)) ||
                    (dy != 0 && FMath.sameSign(dy, e.y - this.y))) {
                    this.x = ox;
                    this.y = oy;
                    return false;
                }
            }
        }

        for (int[] p : level.getTileCollisions(this)) {
            Tile t = Tile.TILES[level.getTile(p[0], p[1])];
            t.bump(level, p[0], p[1], this);

            if (t.collides(level, p[0], p[1], this)) {
                this.x = ox;
                this.y = oy;
                return false;
            }
        }

        return true;
    }

    public boolean canSwimIn(Tile tile, int x, int y) {
        return false;
    }

    public int getCenterX() {
        return this.x + (this.width / 2);
    }

    public int getCenterY() {
        return this.y + (this.height / 2);
    }
}
