package org.gobiiproject.gobiidtomapping.entity.auditable.impl.sampletracking;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapProject;
import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIdListImpl;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoMapProjectImpl implements DtoMapProject {
    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsProjectDao rsProjectDao;

    @Autowired
    private DtoListQueryColl dtoListSampleTrackingQueryColl;


    @Override
    public List<ProjectDTO> getList() throws GobiiDtoMappingException {
        List<ProjectDTO> returnVal;
        try {
            returnVal = (List<ProjectDTO>) dtoListSampleTrackingQueryColl.getList(
                    ListSqlId.QUERY_ID_PROJECT_ALL);
            if(returnVal == null) {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }
        return returnVal;
    }

    public Map<String, String> getProjectCvIdNameMap() throws GobiiDtoMappingException{
        Map<String, String> returnVal = new HashMap<String, String>();
        return returnVal;
    }

    public ProjectDTO get(Integer projectId) throws GobiiDtoMappingException {


        ProjectDTO returnVal = new ProjectDTO();

        try {

            ResultSet resultSet = rsProjectDao.getProjectDetailsForProjectId(projectId);

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one project records for project id: " + projectId));
                }


                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }


    @Override
    public ProjectDTO create(ProjectDTO projectDTO) throws GobiiDtoMappingException {

        ProjectDTO returnVal = projectDTO;



        Map<String, Object> parameters = ParamExtractor.makeParamVals(projectDTO);

        Integer projectId = rsProjectDao.createProject(parameters);
        returnVal.setId(projectId);

        return returnVal;
    }


    @Override
    public ProjectDTO replace(Integer projectId, ProjectDTO projectDTO) throws GobiiDtoMappingException {

        ProjectDTO returnVal = projectDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("projectId", projectId);
            rsProjectDao.updateProject(parameters);


        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public List<ProjectDTO> getProjectsForLoadedDatasets() throws GobiiDtoMappingException {

        List<ProjectDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsProjectDao.getProjectsForLoadedDatasets();
            while (resultSet.next()) {
                ProjectDTO currentProjectDTO = new ProjectDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentProjectDTO);
                returnVal.add(currentProjectDTO);
            }
        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping error", e);
        }


        return  returnVal;

    }


}
