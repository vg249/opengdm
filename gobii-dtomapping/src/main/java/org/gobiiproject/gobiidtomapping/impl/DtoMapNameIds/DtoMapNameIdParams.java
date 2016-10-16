package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdParams {

    private GobiiEntityNameType gobiiEntityNameType;
    private String filterType;
    private String filterValue;

    public DtoMapNameIdParams(GobiiEntityNameType gobiiEntityNameType, String filterType, String filterValue) {
        this.gobiiEntityNameType = gobiiEntityNameType;
        this.filterType = filterType;
        this.filterValue = filterValue;
    }

    public GobiiEntityNameType getEntityType() {
        return gobiiEntityNameType;
    }

    public void setEntity(GobiiEntityNameType entity) {
        this.gobiiEntityNameType = entity;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}
