package org.gobiiproject.gobiimodel.dto.instructions.loader;

/**
 * Created by Phil on 4/12/2016.
 */
public class GobiiFileColumn {

    public enum ColumnType {
        CSV_COLUMN,
        CSV_ROW,
        CSV_BOTH,
        VCF_SAMPLE,
        VCF_MARKER,
        VCF_VARIANT,
        VCF_METADATA,
        VCF_INFO,
        CONSTANT
    }


    private ColumnType columnType = null;
    private Integer rCoord = null;
    private Integer cCoord = null;
    private String name = null;
    private String filterFrom = null;
    private String filterTo = null;
    private String constantValue = null;
    private String index = null;
    private boolean subcolumn = false;
    private String subcolumnDelimiter = null;

    private String metaDataId = null;

    public ColumnType getColumnType() {
        return columnType;
    }

    public GobiiFileColumn setColumnType(ColumnType columnType) {
        this.columnType = columnType;
        return this;
    }

    public Integer getrCoord() {
        return rCoord;
    }

    public GobiiFileColumn setRCoord(Integer rCoord) {
        this.rCoord = rCoord;
        return this;
    }

    public Integer getcCoord() {
        return cCoord;
    }

    public GobiiFileColumn setCCoord(Integer cCoord) {
        this.cCoord = cCoord;
        return this;
    }

    public String getName() {
        return name;
    }

    public GobiiFileColumn setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilterFrom() {
        return filterFrom;
    }

    public GobiiFileColumn setFilterFrom(String filterFrom) {
        this.filterFrom = filterFrom;
        return this;
    }

    public String getFilterTo() {
        return filterTo;
    }

    public GobiiFileColumn setFilterTo(String filterTo) {
        this.filterTo = filterTo;
        return this;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public GobiiFileColumn setConstantValue(String constantValue) {
        this.constantValue = constantValue;
        return this;
    }

    public String getMetaDataId() {
        return metaDataId;
    }

    public GobiiFileColumn setMetaDataId(String metaDataId) {
        this.metaDataId = metaDataId;
        return this;
    }


    public String getIndex() {
        return index;
    }

    public GobiiFileColumn setIndex(String index) {
        this.index = index;
        return this;
    }

    public boolean isSubcolumn() {
        return subcolumn;
    }

    public GobiiFileColumn setSubcolumn(boolean subcolumn) {
        this.subcolumn = subcolumn;
        return this;
    }

    public String getSubcolumnDelimiter() {
        return subcolumnDelimiter;
    }

    public GobiiFileColumn setSubcolumnDelimiter(String subcolumnDelimiter) {
        this.subcolumnDelimiter = subcolumnDelimiter;
        return this;
    }

}
