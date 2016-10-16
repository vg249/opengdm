package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIdFetch;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterTypes;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdFetchAnalyses implements DtoMapNameIdFetch {

    @Autowired
    private RsAnalysisDao rsAnalysisDao = null;
    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchAnalyses.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.ANALYSES;
    }


    private List<NameIdDTO> getAllNameIdsForAnalysis() throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        ResultSet resultSet = rsAnalysisDao.getAnalysisNames();
        List<NameIdDTO> listDTO = new ArrayList<>();

        try {

            while (resultSet.next()) {
                NameIdDTO nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("analysis_id"));
                nameIdDTO.setName(resultSet.getString("name"));
                returnVal.add(nameIdDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private List<NameIdDTO> getNameIdListForAnalysisNameByTypeId(Integer typeId) {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisNamesByTypeId(typeId);

            NameIdDTO nameIdDTO;
            while (resultSet.next()) {
                nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("analysis_id"));
                nameIdDTO.setName(resultSet.getString("name"));
                returnVal.add(nameIdDTO);
            }


        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (LineUtils.isNullOrEmpty(dtoMapNameIdParams.getFilterType())) {
            returnVal = this.getAllNameIdsForAnalysis();
        } else {

            if (dtoMapNameIdParams.getFilterType()
                    .toLowerCase()
                    .equals(GobiiFilterTypes.BYTYPEID.toString()
                            .toLowerCase())) {

                if (!LineUtils.isNullOrEmpty(dtoMapNameIdParams.getFilterValue())) {

                    returnVal = this.getNameIdListForAnalysisNameByTypeId(Integer.parseInt(dtoMapNameIdParams.getFilterValue()));

                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "A filter value was not supplied for the "
                                    + dtoMapNameIdParams.getFilterType()
                                    + " filter on "
                                    + this.getEntityTypeName().toString().toLowerCase());
                }
            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unsupported filter type for "
                                + this.getEntityTypeName().toString().toLowerCase()
                                + ": " + dtoMapNameIdParams.getFilterType());
            }
        }

        return returnVal;
    }
}
