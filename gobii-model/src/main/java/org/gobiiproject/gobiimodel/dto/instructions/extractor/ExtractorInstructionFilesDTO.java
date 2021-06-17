package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;

@Data
public class ExtractorInstructionFilesDTO extends DTOBase {


    private String jobId;

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {
        ;
    }


    private String instructionFileName = null;
    private GobiiExtractProcedure procedure = new GobiiExtractProcedure();

}
