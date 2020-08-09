package com.jdh.microcraft.entity.ai;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntitySlime;
import com.jdh.microcraft.util.Direction;

public class AISlime extends AIHostileMob {
    private final EntitySlime slime;

    private long lastJump;
    private final int jumpDelay;

    public AISlime(EntitySlime slime) {
        super(slime, slime.strength);
        this.slime = slime;
        this.jumpDelay = (int) Math.max(140 - (20 * slime.strength), 60);
    }

    private boolean canJump() {
        return Global.ticks - this.lastJump >= this.jumpDelay;
    }

    @Override
    protected void moveTowards(int x, int y) {
        if (!this.slime.jumping && this.canJump()) {
            this.lastJump = Global.ticks;
            this.slime.jump(x - this.slime.x, y - this.slime.y, 0.5 * slime.strength);
        }
    }

    @Override
    public void tick() {
        super.tick();

        Global.random.setSeed(Global.ticks * this.slime.id);
        if (this.target == null && Global.random.nextInt(180) == 0) {
            this.lastJump = Global.ticks;
            this.slime.jump(Direction.ALL.get(Global.random.nextInt(4)), 0.5);
        }
    }
}
