package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.noaudit.SearchResultDTO;

public interface SearchService {

    SearchResultDTO createSearchQueryResource(String cropType,
                                              String searchQueryString);

}
