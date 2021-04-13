package org.gobiiproject.gobiimodel.dto.gdmv3.templates;

import lombok.Data;
import org.apache.commons.lang.StringUtils;


@Data
public class GenotypeMatrixTemplate {

    // line number that indicates header line
    private Integer headerLineNumber;

    // ignored if headerLineNumber is provided.
    // String that distinctively identifies header line.
    private String headerStartsWith;

    // tells if marker is on left or on top
    private boolean markerAsLeftLabel = true;

    // name of the column which is left side label for matrix.
    private String leftLabelColumnName;

    // file values separator. tab is default separator
    private String separator = "\t";

}
