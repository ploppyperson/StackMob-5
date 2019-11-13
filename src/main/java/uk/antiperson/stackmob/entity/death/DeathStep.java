package uk.antiperson.stackmob.entity.death;

import uk.antiperson.stackmob.entity.StackEntity;

public interface DeathStep {

    int calculateStep();

    void onSpawn(StackEntity spawned);
}
