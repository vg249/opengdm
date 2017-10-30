package org.gobiiproject.gobiidtomapping.instructions;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderFilePreviewDTO;


/**
 * Created by Angel on 11/2016.
 */
public interface DtoMapLoaderFiles {

    LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDtoMappingException, GobiiDaoException;
    LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDtoMappingException, GobiiDaoException;
}
