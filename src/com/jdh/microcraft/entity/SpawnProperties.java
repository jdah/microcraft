package com.jdh.microcraft.entity;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntitySkeleton;
import com.jdh.microcraft.entity.mob.EntitySlime;
import com.jdh.microcraft.entity.mob.EntityZombie;
import com.jdh.microcraft.level.Level;

import java.util.List;
import java.util.function.Function;

public class SpawnProperties {
    public final Class<? extends Entity> cls;
    public final Function<Level, Entity> spawnFunction;
    public final int chance, cap;

    // chance is spawn rate/minute
    // cap and chance are per 128x128 area
    public SpawnProperties(Level level, Class<? extends Entity> cls, Function<Level, Entity> spawnFunction, int chance, int cap) {
        this.cls = cls;
        this.spawnFunction = spawnFunction;

        double scale = ((level.width * level.height) / (128.0 * 128.0)) * Global.game.difficulty;
        this.chance = (int) (chance * scale);
        this.cap = (int) (cap * scale);
    }

    public static List<SpawnProperties> getSpawnProperties(Level level) {
        return switch (level.depth) {
            case 1 -> List.of(
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createBlueSlime, 50, 4),
                new SpawnProperties(level, EntitySkeleton.class, EntitySkeleton::createSkeleton, 100, 4)
            );
            case 0 -> List.of(
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createGreenSlime, 300, 24),
                new SpawnProperties(level, EntityZombie.class, EntityZombie::createEasyZombie, 80, 3)
            );
            case -1 -> List.of(
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createGreenSlime, 200, 24),
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createBlueSlime, 100, 4),
                new SpawnProperties(level, EntityZombie.class, EntityZombie::createEasyZombie, 200, 20),
                new SpawnProperties(level, EntitySkeleton.class, EntitySkeleton::createSkeleton, 200, 24)
            );
            case -2 -> List.of(
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createGreenSlime, 200, 32),
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createBlueSlime, 220, 32),
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createRedSlime, 80, 4),
                new SpawnProperties(level, EntityZombie.class, EntityZombie::createMediumZombie, 200, 24),
                new SpawnProperties(level, EntitySkeleton.class, EntitySkeleton::createSkeleton, 250, 24)
            );
            case -3 -> List.of(
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createGreenSlime, 100, 32),
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createBlueSlime, 100, 32),
                new SpawnProperties(level, EntitySlime.class, EntitySlime::createRedSlime, 400, 32),
                new SpawnProperties(level, EntityZombie.class, EntityZombie::createHardZombie, 250, 32),
                new SpawnProperties(level, EntitySkeleton.class, EntitySkeleton::createSkeleton, 250, 32)
            );
            default -> List.of();
        };
    }
}
