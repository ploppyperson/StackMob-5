package uk.antiperson.stackmob.entity.death;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.concurrent.ThreadLocalRandom;

public class KillStep extends DeathMethod {


    public KillStep(StackMob sm, StackEntity dead) {
        super(sm, dead);
    }

    @Override
    public int calculateStep() {
        int maxStep = getStackMob().getMainConfig().getConfig(getEntity().getType()).getMaxDeathStep();
        int minStep = getStackMob().getMainConfig().getConfig(getEntity().getType()).getMinDeathStep();
        return minStep == maxStep ? maxStep : ThreadLocalRandom.current().nextInt(minStep, maxStep);
    }
}
