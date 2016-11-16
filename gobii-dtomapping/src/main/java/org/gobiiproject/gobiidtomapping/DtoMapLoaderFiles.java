package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapLoaderFiles {

    LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDtoMappingException, GobiiDaoException;
    LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDtoMappingException, GobiiDaoException;
}
