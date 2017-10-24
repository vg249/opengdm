package org.gobiiproject.gobiidtomapping.system;

import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.PingDTO;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapPing {

    PingDTO getPings(PingDTO pingDTO) throws GobiiDtoMappingException;

}
