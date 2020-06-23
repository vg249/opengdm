package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.List;

public interface CallSetService {

    PagedResult<CallSetDTO> getCallSets(Integer pageSize,
                                        Integer pageNum,
                                        Integer variantSetDbId,
                                        CallSetDTO callSetsFilter);

    CallSetDTO getCallSetById(Integer callSetDbId);

    PagedResult<CallSetDTO> getCallSetsBySearchQuery(CallSetsSearchQueryDTO callSetsSearchQuery,
                                                     Integer pageSize, Integer pageNum);

    PagedResult<CallSetDTO> getCallSetsByGenotypesExtractQuery(
        GenotypeCallsSearchQueryDTO genotypesSearchQuery,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;


}
