package uk.antiperson.stackmob.entity.death;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public class KillSingle extends DeathMethod {

    public KillSingle(StackMob sm, StackEntity dead) {
        super(sm, dead);
    }

    @Override
    public int calculateStep() {
        return 1;
    }
}
