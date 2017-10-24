package org.gobiiproject.gobiidtomapping.system;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;


/**
 * Created by Angel on 11/2016.
 */
public interface DtoMapLoaderFiles {

    LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDtoMappingException, GobiiDaoException;
    LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDtoMappingException, GobiiDaoException;
}
