package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;

public interface QCInstructionFilesService {
    QCInstructionsDTO createInstruction(String cropType, QCInstructionsDTO qcInstructionsDTO) throws GobiiDomainException;

    QCInstructionsDTO getInstruction(String cropType, String instructionFileName) throws GobiiDomainException;
}
