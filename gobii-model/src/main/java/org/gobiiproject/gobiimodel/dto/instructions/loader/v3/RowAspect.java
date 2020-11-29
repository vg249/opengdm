package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class RowAspect extends Aspect {

    private final String aspectType = "COLUMN";

    public RowAspect(ColumnCoordinates columnCoordinates) {
        super(columnCoordinates);
    }

    public RowAspect(int row, int column) {
        super(row, column);
    }

    public String getAspectType() {
        return aspectType;
    }

}
