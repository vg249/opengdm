package org.gobiiproject.gobiidtomapping;

/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDtoMappingException extends Exception {

    public GobiiDtoMappingException(Exception e) {
        super(e);
    }

    public GobiiDtoMappingException(String message) {
        super(message);
    }
}
