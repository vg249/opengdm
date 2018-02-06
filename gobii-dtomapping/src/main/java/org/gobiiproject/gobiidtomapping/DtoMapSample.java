package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.SampleDTO;

import java.util.List;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapSample {

    SampleDTO getSampleDetailsByExternalCode(String externalCode) throws GobiiDtoMappingException;

}
