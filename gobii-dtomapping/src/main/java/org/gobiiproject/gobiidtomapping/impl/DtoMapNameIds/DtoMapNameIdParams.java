package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdParams {

    private GobiiEntityNameType gobiiEntityNameType;
    private GobiiFilterType gobiiFilterType;
    private String filterValue;

    public DtoMapNameIdParams(GobiiEntityNameType gobiiEntityNameType, GobiiFilterType gobiiFilterType, String filterValue) {
        this.gobiiEntityNameType = gobiiEntityNameType;
        this.gobiiFilterType = gobiiFilterType;
        this.filterValue = filterValue;
    }

    public GobiiEntityNameType getEntityType() {
        return gobiiEntityNameType;
    }

    public void setEntity(GobiiEntityNameType entity) {
        this.gobiiEntityNameType = entity;
    }

    public GobiiFilterType getGobiiFilterType() {
        return gobiiFilterType;
    }

    public void setGobiiFilterType(GobiiFilterType gobiiFilterType) {
        this.gobiiFilterType = gobiiFilterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}
