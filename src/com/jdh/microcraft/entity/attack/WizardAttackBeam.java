package com.jdh.microcraft.entity.attack;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.ai.AIHostileMob;
import com.jdh.microcraft.entity.mob.EntityAirWizard;
import com.jdh.microcraft.entity.projectile.EntityAirBlast;

public class WizardAttackBeam extends WizardAttack {
    public WizardAttackBeam(EntityAirWizard wizard) {
        super(wizard, 60);
    }

    @Override
    public void tick() {
        super.tick();

        if ((Global.ticks % 2) != 0) {
            return;
        }

        AIHostileMob ai = (AIHostileMob) this.wizard.ai;
        if (ai.target != null) {
            this.wizard.level.addEntity(
                new EntityAirBlast(
                    this.wizard.level, this.wizard,
                    this.wizard.getCenterX(), this.wizard.getCenterY(),
                    ai.target.getCenterX(), ai.target.getCenterY(),
                    1.4,
                    (int) (3 * this.wizard.strength))
            );
        }
    }
}
