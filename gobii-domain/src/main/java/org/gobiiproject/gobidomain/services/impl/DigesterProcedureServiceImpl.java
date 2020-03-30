package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DigesterProcedureSerivce;
import org.gobiiproject.gobiidtomapping.instructions.DtoMapDigesterProcedure;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Phil on 4/12/2016.
 */
public class DigesterProcedureServiceImpl implements DigesterProcedureSerivce {

    private Logger LOGGER = LoggerFactory.getLogger(DigesterProcedureServiceImpl.class);

    @Autowired
    private DtoMapDigesterProcedure dtoMapDigesterProcedure = null;


    @Override
    public LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO LoaderInstructionFilesDTO)
            throws GobiiDomainException {
        LoaderInstructionFilesDTO returnVal;

        returnVal = dtoMapDigesterProcedure.createInstruction(cropType, LoaderInstructionFilesDTO);

        // When we have roles and permissions, this will be set programmatically
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }
} // LoaderInstructionFileServiceImpl
