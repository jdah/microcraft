package com.jdh.microcraft.entity.attack;

import com.jdh.microcraft.entity.mob.EntityAirWizard;
import com.jdh.microcraft.entity.projectile.EntityAirBlast;

public class WizardAttackBurst extends WizardAttack {
    private static final int SIZE = 32;

    private boolean attacked = false;

    public WizardAttackBurst(EntityAirWizard wizard) {
        super(wizard, 8);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attacked) {
            return;
        }

        this.attacked = true;

        for (int i = 0; i < SIZE; i++) {
            int dx = (int) (Math.cos((i / (double) SIZE) * (2 * Math.PI)) * 16),
                dy = (int) (Math.sin((i / (double) SIZE) * (2 * Math.PI)) * 16);

            this.wizard.level.addEntity(
                new EntityAirBlast(
                    this.wizard.level, this.wizard,
                    this.wizard.getCenterX() + dx,
                    this.wizard.getCenterY() + dy,
                    this.wizard.getCenterX() + dx * 10,
                    this.wizard.getCenterY() + dy * 10,
                    1.2,
                    (int) (3 * this.wizard.strength)
                )
            );
        }
    }
}
