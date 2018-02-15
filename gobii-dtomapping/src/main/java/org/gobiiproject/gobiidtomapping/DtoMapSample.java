package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.SampleDTO;

import java.util.List;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapSample {

    SampleDTO getSampleDetailsByExternalCode(String externalCode) throws GobiiDtoMappingException;

}
