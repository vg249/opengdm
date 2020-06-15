package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.SearchResultDTO;

public interface SearchService {

    SearchResultDTO createSearchQueryResource(String cropType,
                                              GenotypeCallsSearchQueryDTO genotypeCallsSearchQuery);

    GenotypeCallsSearchQueryDTO getGenotypesSearchQuery(String resourceId,
                                                        String cropType);
}
