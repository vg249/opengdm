package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterProcedureDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface DigesterProcedureSerivce {
    DigesterProcedureDTO createInstruction(String cropType, DigesterProcedureDTO digesterProcedureDTO) throws GobiiDomainException;
    DigesterProcedureDTO getStatus(String cropType, String instructionFileName) throws GobiiDomainException;
}
