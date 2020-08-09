package com.jdh.microcraft.entity.ai;

import com.jdh.microcraft.entity.mob.EntityZombie;

public class AIZombie extends AIHostileHumanoid {
    public AIZombie(EntityZombie zombie) {
        super(zombie, 0.45 * zombie.strength, zombie.strength);
    }
}
