package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsMarkerMetadataDTO;

import java.util.List;

public interface DtoMapGenotypeCalls {

    List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataList(
            Integer datasetId,
            Integer pageToken,
            Integer pageSize) throws GobiiDtoMappingException;

}
