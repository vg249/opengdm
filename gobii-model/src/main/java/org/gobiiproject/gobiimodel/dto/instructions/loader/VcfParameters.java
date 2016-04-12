package org.gobiiproject.gobiimodel.dto.instructions.loader;

/**
 * Created by Phil on 4/12/2016.
 */
public class VcfParameters {

    private Float maf = null;
    private Float minQ = null;
    private Float minDp = null;
    private boolean removeIndels;
    private boolean   toIupac;

    public Float getMaf() {
        return maf;
    }

    public void setMaf(Float maf) {
        this.maf = maf;
    }

    public Float getMinQ() {
        return minQ;
    }

    public void setMinQ(Float minQ) {
        this.minQ = minQ;
    }

    public Float getMinDp() {
        return minDp;
    }

    public void setMinDp(Float minDp) {
        this.minDp = minDp;
    }

    public boolean isRemoveIndels() {
        return removeIndels;
    }

    public void setRemoveIndels(boolean removeIndels) {
        this.removeIndels = removeIndels;
    }

    public boolean isToIupac() {
        return toIupac;
    }

    public void setToIupac(boolean toIupac) {
        this.toIupac = toIupac;
    }
}
