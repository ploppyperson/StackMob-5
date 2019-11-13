package uk.antiperson.stackmob.entity.death;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public class KillAll extends DeathMethod {

    public KillAll(StackMob sm, StackEntity dead) {
        super(sm, dead);
    }

    @Override
    public int calculateStep() {
        return getDead().getSize();
    }
}
