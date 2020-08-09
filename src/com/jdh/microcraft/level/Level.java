package com.jdh.microcraft.level;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.SpawnProperties;
import com.jdh.microcraft.gfx.Light;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileLiquid;
import com.jdh.microcraft.util.AABB;
import com.jdh.microcraft.util.Time;

import java.util.*;

public class Level {
    // number of expected random ticks per tile per minute
    public static final int RANDOM_TICKS_PER_MINUTE = 10;

    // unique level seed
    public final long seed;

    // level dimensions
    public final int width, height;

    // level depth
    public final int depth;

    // level tiles
    private final int[] tiles;

    // extra tile data
    private final int[] data;

    // current player, null if not yet set
    public EntityPlayer player;

    // all entities in the level
    private final List<Entity> entities = new ArrayList<>();

    // per-tile entities
    private final List<Entity>[] tileEntities;

    // entities which will be added on the next tick
    private final List<Entity> entitiesToAdd = new ArrayList<>();

    // random entity spawn properties
    private List<SpawnProperties> spawnProperties = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Level(long seed, int depth, int width, int height) {
        this.seed = seed;
        this.depth = depth;
        this.width = width;
        this.height = height;
        this.tiles = new int[this.width * this.height];
        this.data = new int[this.width * this.height];
        this.tileEntities = new ArrayList[this.width * this.height];
    }

    public static int toTile(int pixel) {
        return pixel / 16;
    }

    public static int toPixel(int tile) {
        return tile * 16;
    }

    public static int toCenter(int tile) {
        return tile * 16 + 8;
    }

    public int getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            return 0;
        }

        return this.tiles[y * this.width + x];
    }

    public void setTile(int x, int y, int t) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.tiles[y * this.width + x] = t;
            this.setData(x, y, 0);
        }
    }

    // tries to set a tile, doesn't if it produces any collisions
    public boolean trySetTile(int x, int y, int t) {
        int ot = this.getTile(x, y), od = this.getData(x, y);

        // try to set the tile
        this.setTile(x, y, t);

        // check for collisions
        if (this.getTileEntityCollisions(x, y).size() > 0) {
            this.setTile(x, y, ot);
            this.setData(x, y, od);
            return false;
        }

        return true;
    }

    public int getData(int x, int y) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            return 0;
        }

        return this.data[y * this.width + x];
    }

    public void setData(int x, int y, int t) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.data[y * this.width + x] = t;
        }
    }

    public List<Entity> getEntities(int x, int y) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            List<Entity> result = this.tileEntities[y * this.width + x];
            return result == null ? List.of() : result;
        }

        return List.of();
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    private int toIndex(int x, int y) {
        return y * this.width + x;
    }

    public void addEntitySpawns(Collection<SpawnProperties> properties) {
        this.spawnProperties.addAll(properties);
    }

    // populate ALL entity types up to half of their cap
    public void populate() {
        for (SpawnProperties esp : this.spawnProperties) {
            int c = 0, n = 0;
            while (n < (esp.cap * 8) && c < (esp.cap / 2)) {
                c += this.spawn(esp.spawnFunction.apply(this)) ? 1 : 0;
                n++;
            }
        }
    }

    public void removeEntity(int id) {
        var entity = this.entities.stream().filter(e -> e.id == id).findFirst();
        assert (entity.isPresent());
        this.removeEntity(entity.get());
    }

    // IMMEDIATELY removes an entity from the level for transfer to another level.
    // Use Entity.remove to delete an entity.
    public void removeEntity(Entity e) {
        List<Entity> eTileEntities = this.tileEntities[this.toIndex(e.tileX, e.tileY)];
        eTileEntities.remove(e);
        this.entities.remove(e);
    }

    public void addEntity(Entity e) {
        entitiesToAdd.add(e);
    }

    // spawn entity at random valid position, returns false on failure
    public boolean spawn(Entity e) {
        int n = 0;
        while (n < 256) {
            int tx = Global.random.nextInt(this.width), ty = Global.random.nextInt(this.height);

            // no on-screen spawns
            if (Renderer.inBounds(Level.toPixel(tx), Level.toPixel(ty))) {
                continue;
            }

            Tile t = Tile.TILES[this.getTile(tx, ty)];
            if (t instanceof TileLiquid || t.collides(this, tx, ty, e)) {
                continue;
            }

            e.x = Level.toPixel(tx);
            e.y = Level.toPixel(ty);

            if (this.getTileCollisions(e).size() == 0 &&
                this.getEntityCollisions(e).size() == 0) {
                // spawn!
//                Sound.CRAFT.play();
                this.addEntity(e);
                return true;
            }

            n++;
        }

        return false;
    }

    public void spawnOnTile(Entity e, int tile) {
        this.spawnOnTile(e, tile, this.width / 2, this.height / 2);
    }

    public void spawnOnTile(Entity e, int tile, int startX, int startY) {
        int layer = 1, leg = 0, searched = 0, tx = startX, ty = startY;

        // spiral search from center
        while (this.getTile(tx, ty) != tile) {
            switch (leg) {
                case 0 -> {
                    tx++;
                    if (tx == layer) {
                        leg++;
                    }
                }
                case 1 -> {
                    ty++;
                    if (ty == layer) {
                        leg++;
                    }
                }
                case 2 -> {
                    tx--;
                    if (-tx == layer) {
                        leg++;
                    }
                }
                case 3 -> {
                    ty--;
                    if (-ty == layer) {
                        leg = 0;
                        layer++;
                    }
                }
            }

            if (++searched > 2048) {
                System.err.println("WARNING: could not find suitable spawn location");
                tx = this.width / 2;
                ty = this.height / 2;
                break;
            }
        }

        e.x = Level.toPixel(tx);
        e.y = Level.toPixel(ty);
        this.addEntity(e);
    }

    // returns a list of entities which collide with a tile
    public List<Entity> getTileEntityCollisions(int x, int y) {
        List<Entity> result = new ArrayList<>();
        Tile t = Tile.TILES[this.getTile(x, y)];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int tx = x + i, ty = y + j;

                List<Entity> entities = this.getEntities(tx, ty);

                for (Entity e : entities) {
                    int x0 = e.x, y0 = e.y, x1 = e.x + e.width, y1 = e.y + e.width;
                    if (t.collides(this, i, j, e) && AABB.collide(
                        x0, y0, x1, y1,
                        Level.toPixel(x) + t.getCollisionOffsetX(),
                        Level.toPixel(y) + t.getCollisionOffsetY(),
                        Level.toPixel(x) + t.getCollisionOffsetX() + t.getCollisionWidth(),
                        Level.toPixel(y) + t.getCollisionOffsetY() + t.getCollisionHeight()
                    )) {
                        result.add(e);
                    }
                }
            }
        }

        return result;
    }

    // returns list of tile collisions for an entity
    public List<int[]> getTileCollisions(Entity e) {
        List<int[]> result = new ArrayList<>();
        int x0 = e.x, y0 = e.y, x1 = e.x + e.width, y1 = e.y + e.width;

        for (int i = Level.toTile(x0); i <= Level.toTile(x1); i++) {
            for (int j = Level.toTile(y0); j <= Level.toTile(y1); j++) {
                Tile t = Tile.TILES[this.getTile(i, j)];

                if (t.collides(this, i, j, e) && AABB.collide(
                    x0, y0, x1, y1,
                    Level.toPixel(i) + t.getCollisionOffsetX(),
                    Level.toPixel(j) + t.getCollisionOffsetY(),
                    Level.toPixel(i) + t.getCollisionOffsetX() + t.getCollisionWidth(),
                    Level.toPixel(j) + t.getCollisionOffsetY() + t.getCollisionHeight()
                )) {
                    result.add(new int[]{i, j});
                }
            }
        }

        return result;
    }

    // returns the list of entities which collide with the specified entity
    public List<Entity> getEntityCollisions(Entity e) {
        return this.getEntityCollisions(e.x, e.y, e.x + e.width, e.y + e.height);
    }

    // returns the list of colliding entities for the specified box
    public List<Entity> getEntityCollisions(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<>();

        for (int i = Level.toTile(x0); i <= Level.toTile(x1); i++) {
            for (int j = Level.toTile(y0); j <= Level.toTile(y1); j++) {
                List<Entity> entities = this.getEntities(i, j);

                for (Entity e : entities) {
                    if (AABB.collide(
                        x0, y0, x1, y1,
                        e.x, e.y, e.x + e.width, e.y + e.height
                    )) {
                        result.add(e);
                    }
                }
            }
        }

        return result;
    }

    public void update() {
        for (Entity e : entities) {
            e.update();
        }
    }

    // gets lights which *could* influence the viewport aabb
    public List<Light> getLights(AABB aabb) {
        List<Light> lights = new ArrayList<>();

        int tx0 = Level.toTile(aabb.minX) - 8, ty0 = Level.toTile(aabb.minY) - 8,
            tx1 = Level.toTile(aabb.maxX) + 8, ty1 = Level.toPixel(aabb.maxY) + 8;

        for (int y = ty0; y <= ty1; y++) {
            for (int x = tx0; x <= tx1; x++) {
                if (!this.inBounds(x, y)) {
                    continue;
                }

                int power = Tile.TILES[this.getTile(x, y)].getLightPower(this, x, y);
                if (power > 0) {
                    lights.add(new Light(
                        Level.toCenter(x), Level.toCenter(y),
                        power
                    ));
                }

                for (Entity e : this.getEntities(x, y)) {
                    Light l = e.getLight();
                    if (l != null) {
                        lights.add(l);
                    }
                }
            }
        }

        return lights;
    }

    public void render() {
        AABB frustum = Renderer.getAABB();

        int tx0 = Level.toTile(frustum.minX) - 1, ty0 = Level.toTile(frustum.minY) - 1,
            tx1 = Level.toTile(frustum.maxX) + 1, ty1 = Level.toPixel(frustum.maxY) + 1;

        // render tiles
        for (int y = ty0; y <= ty1; y++) {
            for (int x = tx0; x <= tx1; x++) {
                if (!this.inBounds(x, y)) {
                    continue;
                }

                int tile = this.getTile(x, y);

                if (tile == Tile.AIR.id) {
                    continue;
                }

                Tile.TILES[tile].render(this, x, y);
            }
        }

        // render entities in the same tile space
        for (int y = ty0; y <= ty1; y++) {
            for (int x = tx0; x <= tx1; x++) {
                if (!this.inBounds(x, y)) {
                    continue;
                }

                List<Entity> entities = this.tileEntities[this.toIndex(x, y)];

                if (entities == null) {
                    continue;
                }

                // sort by id before rendering so entity order is consistent across tiles
                entities.sort(Comparator.comparingInt(e -> e.id));

                for (Entity e : entities) {
                    e.render();
                }
            }
        }
    }

    // update an entity's current tile if it has changed
    private void updateEntityTile(Entity e) {
        // move entity tile if it has changed
        int tx = Level.toTile(e.x + (e.width / 2)),
            ty = Level.toTile(e.y + (e.height / 2));

        if (e.tileX != tx || e.tileY != ty) {
            List<Entity>
                currentList = this.inBounds(e.tileX, e.tileY) ?
                this.tileEntities[this.toIndex(e.tileX, e.tileY)] : null,
                newList = this.inBounds(tx, ty) ? this.tileEntities[this.toIndex(tx, ty)] : null;

            if (currentList != null) {
                assert (currentList.contains(e));
                currentList.remove(e);

                if (currentList.size() == 0) {
                    this.tileEntities[this.toIndex(e.tileX, e.tileY)] = null;
                }
            }

            if (newList == null && this.inBounds(tx, ty)) {
                newList = new ArrayList<>();
                this.tileEntities[this.toIndex(tx, ty)] = newList;
            }

            if (newList != null) {
                newList.add(e);
                Tile.TILES[this.getTile(tx, ty)].step(this, tx, ty, e);
            }
        }

        e.tileX = tx;
        e.tileY = ty;
    }

    public void tick() {
        // random tile ticks
        Global.random.setSeed(Time.now());
        for (int i = 0; i < ((this.width * this.height) / (Time.TPS * 60)) * RANDOM_TICKS_PER_MINUTE; i++) {
            int tx = Global.random.nextInt(this.width), ty = Global.random.nextInt(this.height);
            Tile.TILES[this.getTile(tx, ty)].randomTick(this, tx, ty);
        }

        // random entity spawns
        for (SpawnProperties esp : this.spawnProperties) {
            if (Global.random.nextInt((60 * 60 * 60) / esp.chance) == 0 &&
                this.entities.stream()
                    .filter(e -> e.getClass().isAssignableFrom(esp.cls))
                    .count() < esp.cap) {
                this.spawn(esp.spawnFunction.apply(this));
            }
        }

        for (Entity e : this.entities) {
            this.updateEntityTile(e);
            e.tick();
            this.updateEntityTile(e);
        }

        // add requested entities
        for (Entity e : this.entitiesToAdd) {
            if (e instanceof EntityPlayer) {
                this.player = (EntityPlayer) e;
            }

            this.entities.add(e);
        }
        this.entitiesToAdd.clear();

        // remove/move entities which have requested it
        for (var it = this.entities.iterator(); it.hasNext(); ) {
            Entity e = it.next();

            if (e.metadata.remove || e.metadata.moveDepthOnNextTick) {

                if (this.inBounds(e.tileX, e.tileY)) {
                    List<Entity> es = this.tileEntities[this.toIndex(e.tileX, e.tileY)];
                    if (es != null) {
                        assert (es.contains(e));
                        es.remove(e);
                    }
                }

                it.remove();

                if (e.metadata.moveDepthOnNextTick) {
                    // move to other level depth
                    Level newLevel = Global.game.getLevel(e.metadata.moveToDepth);
                    newLevel.addEntity(e);
                    e.tileX = -1;
                    e.tileY = -1;
                    e.x = e.metadata.moveToX;
                    e.y = e.metadata.moveToY;
                    e.metadata.moveDepthOnNextTick = false;
                    e.level = newLevel;
                    e.onDepthChange(this.depth);

                    if (e instanceof EntityPlayer) {
                        this.player = null;
                    }
                }
            }
        }
    }
}
