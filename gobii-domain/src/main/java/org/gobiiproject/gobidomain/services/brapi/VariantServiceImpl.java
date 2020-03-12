package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.PageToken;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class VariantServiceImpl implements VariantService {

    Logger LOGGER = LoggerFactory.getLogger(GenotypeCallsService.class);

    @Autowired
    protected MarkerDao markerDao;

    @Override
    public PagedResult<VariantDTO> getVariants(Integer pageSize, String pageToken,
                                               Integer variantDbId, Integer variantSetDbId) {

        PagedResult<VariantDTO> returnVal = new PagedResult<>();

        List<VariantDTO> variants = new ArrayList<>();

        Integer markerIdCursor = null;

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");

            if(pageToken != null) {
                Map<String, Integer> pageTokenParts = PageToken.decode(pageToken);
                markerIdCursor = pageTokenParts.get("markerIdCursor");
            }


            List<Marker> markers = markerDao.getMarkersByMarkerIdCursor(
                    pageSize, markerIdCursor, variantSetDbId);

            for(Marker marker : markers) {

                VariantDTO variantDTO = new VariantDTO();

                ModelMapper.mapEntityToDto(marker, variantDTO);

                variants.add(variantDTO);
            }

            returnVal.setResult(variants);

            returnVal.setCurrentPageSize(pageSize);

            //Set page token only if there are variants
            if(markers.size() > 0) {

                Map<String, Integer> nextPageCursorMap = new HashMap<>();

                //set next page offset and column offset as page token parts
                nextPageCursorMap.put("markerIdCursor",
                        markers.get(markers.size() - 1).getMarkerId());

                String nextPageToken = PageToken.encode(nextPageCursorMap);

                returnVal.setNextPageToken(nextPageToken);

            }

            return  returnVal;

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }



    }

    @Override
    public VariantDTO getVariantByVariantDbId(Integer variantDbId) {
        VariantDTO returnVal = new VariantDTO();
        return returnVal;
    }




}
