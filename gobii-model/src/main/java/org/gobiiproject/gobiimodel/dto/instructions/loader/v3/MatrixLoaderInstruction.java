package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import lombok.Data;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.GenotypeMatrixTemplate;

@Data
public class MatrixLoaderInstruction {

    private String fileType;

    private String dataFormat;

    private boolean loadMarkers;

    private GenotypeMatrixTemplate template;

}
