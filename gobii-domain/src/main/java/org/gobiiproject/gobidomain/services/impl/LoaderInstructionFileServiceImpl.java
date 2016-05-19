package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderInstructionFileServiceImpl implements LoaderInstructionFilesService {

    @Autowired
    private DtoMapLoaderInstructions dtoMapLoaderInstructions = null;


    @Override
    public LoaderInstructionFilesDTO processLoaderFileInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        switch(returnVal.getProcessType() ) {

            case CREATE:
                returnVal = dtoMapLoaderInstructions.writeInstructions(loaderInstructionFilesDTO);
                break;

            case READ:
                returnVal = dtoMapLoaderInstructions.readInstructions(loaderInstructionFilesDTO);
                break;

            default:
                throw new GobiiDtoMappingException(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                        "Unsupported proces type " + returnVal.getProcessType().toString());

        } // switch

        return returnVal;

    } // processLoaderFileInstructions

} // LoaderInstructionFileServiceImpl
