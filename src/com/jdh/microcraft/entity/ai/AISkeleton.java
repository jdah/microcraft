package com.jdh.microcraft.entity.ai;

import com.jdh.microcraft.entity.mob.EntitySkeleton;

public class AISkeleton extends AIHostileHumanoid {
    private final EntitySkeleton skeleton;

    public AISkeleton(EntitySkeleton skeleton) {
        super(skeleton, 0.5 * skeleton.strength, skeleton.strength);
        this.skeleton = skeleton;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.target != null) {
            this.skeleton.shoot(this.target.x, this.target.y);
        }
    }

    protected double getPreferredTargetDistance() {
        return 16 * 3;
    }

    protected double getPreferredTargetDistanceThreshold() {
        return 24;
    }
}
