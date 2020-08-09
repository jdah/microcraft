package com.jdh.microcraft.level;

import com.jdh.microcraft.entity.Entity;

public class LevelEntityMetadata {
    // entity owned by this metadata
    public Entity entity;

    // if true, entity is removed from the level on the next tick
    public boolean remove;

    // if set, entity is moved to the specified level depth on the next tick
    public int moveToX, moveToY;
    public int moveToDepth;
    public boolean moveDepthOnNextTick;

    public LevelEntityMetadata(Entity entity) {
        this.entity = entity;
    }

    public void moveLevel(int depth, int x, int y)  {
        if (this.entity.level.depth == depth) {
            return;
        }

        this.moveDepthOnNextTick = true;
        this.moveToDepth = depth;
        this.moveToX = x;
        this.moveToY = y;
    }
}
