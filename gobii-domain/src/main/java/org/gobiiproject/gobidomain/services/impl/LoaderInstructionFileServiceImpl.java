package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderInstructionFileServiceImpl implements LoaderInstructionFilesService {

    private Logger LOGGER = LoggerFactory.getLogger(LoaderInstructionFileServiceImpl.class);

    @Autowired
    private DtoMapLoaderInstructions dtoMapLoaderInstructions = null;


    @Override
    public List<LoaderInstructionFilesDTO> getInstructions(String getInstructions) throws GobiiDomainException {

        List<LoaderInstructionFilesDTO> returnVal;

        try {
            returnVal = dtoMapLoaderInstructions.getInstructions(getInstructions);
            for(LoaderInstructionFilesDTO currentLoaderInstructionFilesDTO : returnVal ) {
                currentLoaderInstructionFilesDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public LoaderInstructionFilesDTO createInstruction(LoaderInstructionFilesDTO LoaderInstructionFilesDTO) throws GobiiDomainException {
        LoaderInstructionFilesDTO returnVal;

        try {

            returnVal = dtoMapLoaderInstructions.createInstruction(LoaderInstructionFilesDTO);

            // When we have roles and permissions, this will be set programmatically
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
        return returnVal;
    }
} // LoaderInstructionFileServiceImpl
