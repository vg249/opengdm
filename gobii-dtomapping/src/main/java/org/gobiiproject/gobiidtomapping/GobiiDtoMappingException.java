package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.header.Status;

/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDtoMappingException extends GobiiException {


    public GobiiDtoMappingException(Status.StatusLevel statusLevel,
                                    Status.ValidationStatusType validationStatusType,
                                    String message) {
        super(statusLevel,validationStatusType,message);
    }
}
