package org.gobiiproject.gobidomain.services.brapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.PageToken;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.utils.JsonNodeUtils;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
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
                    pageSize, markerIdCursor,
                    variantDbId, variantSetDbId);

            for(Marker marker : markers) {

                VariantDTO variantDTO = new VariantDTO();

                ModelMapper.mapEntityToDto(marker, variantDTO);

                if(marker.getDatasetMarkerIdx() != null) {

                    List<String> variantSetDbIds = JsonNodeUtils.getListFromIterator(
                            marker.getDatasetMarkerIdx().fieldNames());

                    variantDTO.setVariantSetDbId(variantSetDbIds);

                }

                if(marker.getMarkerName() != null) {
                    variantDTO.setVariantNames(new ArrayList<>());
                    variantDTO.getVariantNames().add(marker.getMarkerName());
                }

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

        try {

            Objects.requireNonNull(variantDbId, "variantDbId : Required non null");

            Marker marker = markerDao.getMarkerById(variantDbId);

            ModelMapper.mapEntityToDto(marker, returnVal);

            if(marker.getDatasetMarkerIdx() != null) {

                List<String> variantSetDbIds = JsonNodeUtils.getListFromIterator(
                        marker.getDatasetMarkerIdx().fieldNames());

                returnVal.setVariantSetDbId(variantSetDbIds);

            }

            return returnVal;

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

    }




}
