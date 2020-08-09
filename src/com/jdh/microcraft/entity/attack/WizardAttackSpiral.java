package com.jdh.microcraft.entity.attack;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntityAirWizard;
import com.jdh.microcraft.entity.projectile.EntityAirBlast;

public class WizardAttackSpiral extends WizardAttack {
    public WizardAttackSpiral(EntityAirWizard wizard) {
        super(wizard, 240);
    }

    @Override
    public void tick() {
        super.tick();

        if ((Global.ticks % 2) != 0) {
            return;
        }

        int dx = (int) (Math.cos(((Global.ticks % 60) / 60.0) * (2 * Math.PI)) * 16),
            dy = (int) (Math.sin(((Global.ticks % 60) / 60.0) * (2 * Math.PI)) * 16);

        this.wizard.level.addEntity(
            new EntityAirBlast(
                this.wizard.level, this.wizard,
                this.wizard.getCenterX() + dx,
                this.wizard.getCenterY() + dy,
                this.wizard.getCenterX() + dx * 10,
                this.wizard.getCenterY() + dy * 10,
                1.4,
                (int) (3 * this.wizard.strength)
            )
        );
    }
}
