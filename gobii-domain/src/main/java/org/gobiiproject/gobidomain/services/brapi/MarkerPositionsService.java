package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.MarkerPositions;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.math.BigDecimal;

public interface MarkerPositionsService {

    PagedResult<MarkerPositions> getMarkerPositions(
        Integer pageSize, Integer pageNum,
        MarkerPositions markerPositionsFilter, BigDecimal minPosition,
        BigDecimal maxPosition, Integer variantSetDbId);

}
