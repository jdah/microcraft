package com.jdh.microcraft.entity.ai;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntityHumanoid;
import com.jdh.microcraft.util.Direction;
import com.jdh.microcraft.util.FMath;

public class AIHostileHumanoid extends AIHostileMob {
    private final EntityHumanoid humanoid;
    private final double speed, strength;

    private Direction randomMoveDirection;

    public AIHostileHumanoid(EntityHumanoid humanoid, double speed, double strength) {
        super(humanoid, strength);
        this.humanoid = humanoid;
        this.speed = speed;
        this.strength = strength;
    }

    @Override
    protected void moveTowards(int x, int y) {
        double dx = FMath.sign(x - this.humanoid.x),
            dy = FMath.sign(y - this.humanoid.y),
            l = FMath.norm(dx, dy);

        dx /= l;
        dy /= l;
        dx = Double.isNaN(dx) ? 0 : dx;
        dy = Double.isNaN(dy) ? 0 : dy;

        dx *= this.speed;
        dy *= this.speed;

        if (!this.humanoid.swimming || Global.ticks % 2 == 0) {
            int mx = FMath.tickedDoubleToInt(Global.ticks, dx),
                my = FMath.tickedDoubleToInt(Global.ticks, dy);

            if (this.shouldMove(mx, 0)) {
                this.humanoid.move(mx, 0);
            }

            if (this.shouldMove(0, my)) {
                this.humanoid.move(0, my);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.target == null) {
            if (this.randomMoveDirection == null && Global.random.nextInt(240) == 0) {
                this.randomMoveDirection = Direction.ALL.get(Global.random.nextInt(4));
            } else if (this.randomMoveDirection != null) {
                this.moveTowards(
                    this.humanoid.x + this.randomMoveDirection.x * 10,
                    this.humanoid.y + this.randomMoveDirection.y * 10
                );

                if (Global.random.nextInt(120) == 0) {
                    this.randomMoveDirection = null;
                }
            }
        }
    }
}
