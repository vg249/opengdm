package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface DigesterProcedureSerivce {
    LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiDomainException;
}
