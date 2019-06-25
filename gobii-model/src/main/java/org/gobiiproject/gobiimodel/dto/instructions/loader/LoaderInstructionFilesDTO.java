package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoaderInstructionFilesDTO extends DTOBase {


    private GobiiLoaderMetadata metadata;
    private List<GobiiLoaderInstruction> gobiiLoaderInstructions = new ArrayList<>();
    private String instructionFileName = null;


    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {
        ;
    }

}
