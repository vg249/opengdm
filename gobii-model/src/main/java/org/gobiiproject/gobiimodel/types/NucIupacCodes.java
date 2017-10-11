package org.gobiiproject.gobiimodel.types;

/**
 * Created by siva on 18-05-2017.
 */
public enum NucIupacCodes {
    AA,
    TT,
    GG,
    CC,
    NN,
    AT,
    AG,
    AC,
    TG,
    TC,
    GC,
    plusminus;
    public String getName(){
        if(this.equals(plusminus))return "+-";
        return this.name();
    }
}

