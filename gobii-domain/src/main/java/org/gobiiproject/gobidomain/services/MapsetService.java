package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;

import java.util.List;

/**
 * Created by Phil on 4/28/2016.
 */
public interface MapsetService {

    MapsetDTO processMapset(MapsetDTO MapsetDTO);

    ResultEnvelope<List<MapsetDTO>> getAllMapsetNames ();

}
