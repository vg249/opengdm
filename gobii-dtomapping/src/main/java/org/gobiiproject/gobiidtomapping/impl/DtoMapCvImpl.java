package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapCvImpl implements DtoMapCv {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvImpl.class);


    @Autowired
    private RsCvDao rsCvDao = null;

    @Autowired
    DtoMapCvGroup dtoMapCvGroup;

    public final Integer GROUP_TYPE_SYSTEM = 1;
    public final Integer GROUP_TYPE_USER = 2;

    @Override
    public List<CvDTO> getCvs() throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsCvDao.getCvNames();
            while (resultSet.next()) {
                CvDTO currentCvDTO = new CvDTO();
                currentCvDTO.setTerm(resultSet.getString("term"));
                currentCvDTO.setCvId(resultSet.getInt("cv_id"));
                currentCvDTO.setGroupType(resultSet.getInt("group_type"));
                returnVal.add(currentCvDTO);
            }
        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public CvDTO getCvDetails(Integer cvId) throws GobiiDtoMappingException {

        CvDTO returnVal = new CvDTO();

        try {

            ResultSet resultSet = rsCvDao.getDetailsForCvId(cvId);

            if (resultSet.next()) {
                // apply cv values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }


    @Override
    public CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException {
        CvDTO returnVal = cvDTO;

        Integer groupIdForUserGroup = this.getUserCvGroupId(cvDTO);

        if (groupIdForUserGroup > 0 ) {

            returnVal.setGroup(groupIdForUserGroup);
            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer cvId = rsCvDao.createCv(parameters);
            returnVal.setCvId(cvId);

        } else {

            throw new GobiiDtoMappingException("There is no user cvgroiup for the specified CV group: " + cvDTO.getCvId().toString());

        }

        return returnVal;
    }

    private Integer getUserCvGroupId(CvDTO cvDto) throws GobiiDtoMappingException {

        Integer returnVal;

        if (cvDto != null) {

            if (cvDto.getGroupId() != null && cvDto.getGroupId() > 0) {

                CvGroupDTO cvGroupDTOCandidate = this.dtoMapCvGroup.getCvGroup(cvDto.getGroupId());
                if (cvGroupDTOCandidate.getGroupType().equals(this.GROUP_TYPE_USER)) {

                    returnVal = cvGroupDTOCandidate.getCvGroupId();

                } else {

                    CvGroupDTO cvGroupDTOUser = this.dtoMapCvGroup.getUserCvByGroupName(cvGroupDTOCandidate.getName());
                    returnVal = cvGroupDTOUser.getCvGroupId();
                }

            } else {
                throw new GobiiDtoMappingException("The input CvDTO does not have a group id");
            }

        } else {
            throw new GobiiDtoMappingException("The input CvDTO is null");
        }

        return returnVal;
    }


    @Override
    public CvDTO replaceCv(Integer cvId, CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        Integer groupIdForUserGroup = this.getUserCvGroupId(cvDTO);

        if (groupIdForUserGroup > 0 ) {

            CvDTO currentCvDTO = getCvDetails(cvId);

            if (currentCvDTO.getCvId() > 0 ) {

                cvDTO.setGroup(groupIdForUserGroup);
                cvDTO.setCvId(cvId);
                Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
                rsCvDao.updateCv(parameters);

            } else {

                LOGGER.error("There is no cv with id " + cvId.toString());
                throw new GobiiDtoMappingException("There is no cv with id " + cvId.toString());
            }

        } else {

            throw new GobiiDtoMappingException("There is no user cvgroup for the specified CV group: " + cvDTO.getCvId().toString());

        }

        return returnVal;
    }

    @Override
    public CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        if (cvDTO.getGroupType().equals(GROUP_TYPE_USER)) {

            returnVal.setEntityStatus(0);
            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsCvDao.deleteCv(parameters);

            returnVal.setCvId(-1);
            returnVal.setGroupId(null);
            returnVal.setXrefId(null);
            returnVal.setTerm(null);
            returnVal.setAbbreviation(null);
            returnVal.setDefinition(null);
            returnVal.setRank(null);

        } else {

            LOGGER.error("Cannot delete cv term that belongs to a system group");
            throw new GobiiDtoMappingException("The specified cvId ("
                    + cvDTO.getCvId()
                    + ") belongs to a cvgroup of type system");

        }

        return returnVal;
    }

    @Override
    public List<CvDTO> getCvsByGroupName(String groupName) throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvDao.getCvsByGroup(groupName);

            while (resultSet.next()) {

                CvDTO currentCvDTO = new CvDTO();

                ResultColumnApplicator.applyColumnValues(resultSet, currentCvDTO);
                returnVal.add(currentCvDTO);

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

} // DtoMapNameIdListImpl
