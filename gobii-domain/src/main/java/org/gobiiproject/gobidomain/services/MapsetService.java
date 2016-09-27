package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;

import java.util.List;

/**
 * Created by Phil on 4/28/2016.
 */
public interface MapsetService {

    //MapsetDTO processMapset(MapsetDTO MapsetDTO);

    List<MapsetDTO> getAllMapsetNames ()  throws GobiiDomainException;

}
