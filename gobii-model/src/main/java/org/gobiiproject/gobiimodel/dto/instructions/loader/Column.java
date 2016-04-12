package org.gobiiproject.gobiimodel.dto.instructions.loader;

/**
 * Created by Phil on 4/12/2016.
 */
public class Column {

    public enum ColumnType {CSV_COLUMN, CSV_ROW, CSV_BOTH, VCF_SAMPLE, VCF_MARKER, VCF_VARIANT, VCF_METADATA}


    private ColumnType columnType = null;
    private Integer rCoord = null;
    private Integer cCoord = null;
    private String name = null;
    private String filterFrom = null;
    private String filterTo = null;

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Integer getrCoord() {
        return rCoord;
    }

    public void setrCoord(Integer rCoord) {
        this.rCoord = rCoord;
    }

    public Integer getcCoord() {
        return cCoord;
    }

    public void setCCoord(Integer cCoord) {
        this.cCoord = cCoord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilterFrom() {
        return filterFrom;
    }

    public void setFilterFrom(String filterFrom) {
        this.filterFrom = filterFrom;
    }

    public String getFilterTo() {
        return filterTo;
    }

    public void setFilterTo(String filterTo) {
        this.filterTo = filterTo;
    }


}
