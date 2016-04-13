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

    public Column setColumnType(ColumnType columnType) {
        this.columnType = columnType;
        return this;
    }

    public Integer getrCoord() {
        return rCoord;
    }

    public Column setRCoord(Integer rCoord) {
        this.rCoord = rCoord;
        return this;
    }

    public Integer getcCoord() {
        return cCoord;
    }

    public Column setCCoord(Integer cCoord) {
        this.cCoord = cCoord;
        return this;
    }

    public String getName() {
        return name;
    }

    public Column setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilterFrom() {
        return filterFrom;
    }

    public Column setFilterFrom(String filterFrom) {
        this.filterFrom = filterFrom;
        return this;
    }

    public String getFilterTo() {
        return filterTo;
    }

    public Column setFilterTo(String filterTo) {
        this.filterTo = filterTo;
        return this;
    }


}
