package com.jdh.microcraft.entity.ai;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileLava;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;
import com.jdh.microcraft.util.Time;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class AIHostileMob extends AI {
    protected final double sightDistance;
    protected final int targetPermanenceTicks;

    protected final double strength;
    protected int ticksSinceTargetSeen;
    public Entity target;

    public AIHostileMob(Entity entity, double strength) {
        super(entity);
        this.strength = strength;
        this.sightDistance = (16 * 10) * this.strength;
        this.targetPermanenceTicks = (int) (180 * this.strength);
    }

    // avoid really dangerous stuff
    protected boolean shouldMove(int dx, int dy) {
        int cx = this.entity.getCenterX() + dx,
            cy = this.entity.getCenterY() + dy,
            tx = Level.toTile(cx),
            ty = Level.toTile(cy);

        return !(Tile.TILES[this.entity.level.getTile(tx, ty)] instanceof TileLava);
    }

    protected abstract void moveTowards(int x, int y);

    @Override
    public void tick() {
        super.tick();

        if (this.entity.level.player != null &&
            FMath.norm(this.entity.x - this.entity.level.player.x,
                this.entity.y - this.entity.level.player.y) <= this.sightDistance) {
            this.target = this.entity.level.player;
            this.ticksSinceTargetSeen = 0;
        } else {
            this.ticksSinceTargetSeen++;

            // lose target if it has been too long
            if (this.ticksSinceTargetSeen >= this.targetPermanenceTicks) {
                this.target = null;
            }
        }

        if (this.target != null) {
            int tcx = this.target.x + (this.target.width / 2),
                tcy = this.target.y + (this.target.height / 2),
                tdx = tcx - this.entity.x,
                tdy = tcy - this.entity.y;

            double pd = this.getPreferredTargetDistance(),
                pdt = this.getPreferredTargetDistanceThreshold();

            if (Math.abs(tdx) - pd > pdt ||
                Math.abs(tdy) - pd > pdt) {
                this.moveTowards(this.target.x, this.target.y);
            } else if (pd != 0.0 && pdt != 0.0) {
                // ranged mobs: strafe in directions not towards the target
                Global.random.setSeed(Global.ticks / Time.TPS);
                if (Global.random.nextInt(3) == 0) {
                    Direction td = Direction.get(tdx, tdy);
                    Direction strafeDir = new ArrayList<>(Direction.ALL)
                        .stream()
                        .filter(d -> d != td && d != td.opposite())
                        .collect(Collectors.toList())
                        .get(Global.random.nextInt(2));
                    this.moveTowards(this.entity.x + strafeDir.x, this.entity.y + strafeDir.y);
                }
            }
        }
    }

    protected double getPreferredTargetDistance() {
        return 0.0;
    }

    protected double getPreferredTargetDistanceThreshold() {
        return 0.0;
    }
}
