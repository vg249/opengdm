package org.gobiiproject.gobidomain.services.brapi;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.SearchResultDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    protected ConfigSettingsService configSettingsService;

    @Override
    public SearchResultDTO createSearchQueryResource(String cropType,
                                                     GenotypeCallsSearchQueryDTO genotypeCallsSearchQueryDTO) {

        if (cropType == null) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error: Unable to read crop configuration");
        }

        SearchResultDTO searchResultDTO = new SearchResultDTO();

        ObjectMapper objectMapper  = new ObjectMapper();

        try {

            String resourceId = UUID.randomUUID().toString();

            String extractQueryPath = getSearchQueryResourcePath(resourceId, cropType);

            File extractQueryFile = new File(extractQueryPath);

            extractQueryFile.getParentFile().mkdirs();

            objectMapper.writeValue(extractQueryFile, genotypeCallsSearchQueryDTO);

            searchResultDTO.setSearchResultDbId(resourceId);

            return searchResultDTO;

        } catch (Exception e) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error");

        }
    }

    private String getSearchQueryResourcePath(String resourceId, String cropType) {
        return Paths.get(
                configSettingsService.getConfigSettings().getServerConfigs().get(
                        cropType).getFileLocations().get(GobiiFileProcessDir.RAW_USER_FILES),
                resourceId,
                "searchQuery.json").toString();
    }

    @Override
    public GenotypeCallsSearchQueryDTO getGenotypesSearchQuery(String resourceId, String cropType) {

        GenotypeCallsSearchQueryDTO genotypeCallsSearchQuery;
        try {

            ObjectMapper mapper = new ObjectMapper();

            String extractQueryPath = getSearchQueryResourcePath(resourceId, cropType);

            File extractQueryFile = new File(extractQueryPath);

            genotypeCallsSearchQuery = mapper.readValue(extractQueryFile, GenotypeCallsSearchQueryDTO.class);

            return genotypeCallsSearchQuery;

        } catch (Exception e) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error");

        }
    }
}
