package uk.antiperson.stackmob.entity.death;

public enum DeathType {
    SINGLE(KillSingle.class),
    ALL(KillAll.class),
    STEP(KillStep.class),
    STEP_DAMAGE(KillStepDamage.class);

    private final Class<? extends DeathMethod> dclass;

    DeathType(Class<? extends DeathMethod> dclass) {
        this.dclass = dclass;
    }

    public Class<? extends DeathMethod> getStepClass() {
        return dclass;
    }
}
