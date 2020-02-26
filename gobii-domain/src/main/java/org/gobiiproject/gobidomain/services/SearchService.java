package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.noaudit.SearchResultDTO;

public interface SearchService {

    SearchResultDTO createSearchQueryResource(String cropType,
                                              String searchQueryString);

}
