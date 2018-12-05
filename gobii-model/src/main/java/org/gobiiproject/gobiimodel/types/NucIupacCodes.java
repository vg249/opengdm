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
    plusminus,
    plus,
    minus;
    public String getName(){
        if(this.equals(plusminus))return "+-";
        else if(this.equals(plus))return "++";
        else if(this.equals(minus))return "--";
        return this.name();
    }
}
