package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DTOBase {


    private GobiiLoaderProcedure procedure = new GobiiLoaderProcedure();
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
