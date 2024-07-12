package uk.antiperson.stackmob.mspt;

public abstract class MsptProvider {

    boolean underLoad;

    public abstract double getMspt();
    public void setUnderLoad(boolean underLoad) {
        this.underLoad = underLoad;
    }
    public boolean isUnderLoad() {
        return this.underLoad;
    }

}
