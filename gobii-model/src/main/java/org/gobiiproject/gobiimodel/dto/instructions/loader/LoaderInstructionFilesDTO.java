package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DTOBase {


    private GobiiLoaderInstruction primaryLoaderInstruction = new GobiiLoaderInstruction();
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

    public List<GobiiLoaderInstruction> getGobiiLoaderInstructions() {
        return gobiiLoaderInstructions;
    }

    public void setGobiiLoaderInstructions(List<GobiiLoaderInstruction> gobiiLoaderInstructions) {
        this.gobiiLoaderInstructions = gobiiLoaderInstructions;
    }

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }



}
