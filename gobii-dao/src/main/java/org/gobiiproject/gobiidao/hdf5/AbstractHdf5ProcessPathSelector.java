package org.gobiiproject.gobiidao.hdf5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 5/25/2016.
 */
public abstract class AbstractHdf5ProcessPathSelector {

    Map<String, Object> hdf5ProcessingPathByCrop = new HashMap<>();

    public void setHdf5ProcessingPathByCrop(Map<String, Object> hdf5ProcessingPathByCrop) {
        this.hdf5ProcessingPathByCrop = hdf5ProcessingPathByCrop;
    }

    public Object getHdf5ProcessPaths(String cropType) {
        return this.hdf5ProcessingPathByCrop.get(cropType);
    }

    protected String determineCurrentLookupKey() {
        String returnVal = null;
        return returnVal;
    }

}
