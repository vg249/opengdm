package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DTOBase {


    private GobiiLoaderProcedure procedure;
    private String instructionFileName = null;


    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {
        ;
    }

    public GobiiLoaderProcedure getProcedure() {
        return procedure;
    }

    public void setGobiiLoaderProcedure(GobiiLoaderProcedure procedure) {
        this.procedure = procedure;
    }

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }



}
