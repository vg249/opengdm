package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"aspectType", "columnCoordinates"})
public class RowAspect extends Aspect {

    public final String aspectType = "ROW";

    public RowAspect(ColumnCoordinates columnCoordinates) {
        super(columnCoordinates);
    }

    public RowAspect(int row, int column) {
        super(row, column);
    }

}
