package uk.antiperson.stackmob.mspt;

public interface MsptProvider {

    double getMspt();
    void setUnderLoad(boolean underLoad);
    boolean isUnderLoad();

}
