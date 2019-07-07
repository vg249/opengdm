package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;

import java.util.List;

public interface DtoMapGenotypeCalls {

    List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataList(
            Integer datasetId,
            Integer pageToken,
            Integer pageSize) throws GobiiDtoMappingException;

    List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataList(
            Integer datasetId,
            Integer pageToken,
            Integer pageSize) throws GobiiDtoMappingException;

    List<GenotypeCallsDTO> getGenotypeCallsList(DnaRunDTO dnarun, String pageToken,
                                                Integer pageSize, String outputDirPath);

    List<GenotypeCallsDTO> getGenotypeCallsList(MarkerBrapiDTO marker, String pageToken,
                                                Integer pageSize, String outputDirPath);

}
