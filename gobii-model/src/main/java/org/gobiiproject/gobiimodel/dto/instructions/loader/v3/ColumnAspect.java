package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class ColumnAspect {

    private final String aspectType = "COLUMN";

    private ColumnCoordinates columnCoordinates;

    public String getAspectType() {
        return aspectType;
    }

    public ColumnCoordinates getColumnCoordinates() {
        return columnCoordinates;
    }

    public void setColumnCoordinates(ColumnCoordinates columnCoordinates) {
        this.columnCoordinates = columnCoordinates;
    }
}
