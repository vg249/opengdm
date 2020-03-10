package org.gobiiproject.gobiimodel.dto.auditable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.children.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;


/**
 * Created by Phil on 4/6/2016.
 */
public class V3ProjectDTO extends DTOBaseAuditable {

    /**
     *  {
                "projectId": "1",
                "piContactId": 34,
                "piContactName" : "imPi",
                "projectName": "foo_proj_01",
                "projectDescription": "foo 01 project",
                "experimentCount" : 5,
                "datasetCount" : 6,
                "markersCount" : 15000,
                "dnaRunsCount" : 1000,
                "createdBy": "1",
                "createdDate": "2019-07-25T04:00:00",
                "modifiedBy": "1",
                "modifiedDate": "2019-07-25T04:00:00",
                "properties": [
                    {
                        "propertyId" : "1",
                        "propertyName" : "testProp",
                        "propertyValue" : "testPropValue"
                    },
                ]
            },
     */
    public V3ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
    }

    @Override
    public Integer getId() {
        return this.projectId;
    }

    @Override
    public void setId(Integer id) {
        this.projectId = id;
    }


    // we are waiting until we a have a view to return
    // properties for that property: we don't know how to represent them yet

    private Integer projectId = 0;
    private String projectName;
    private String projectDescription;
    private Integer piContactId;
    private String piContactName;
    private Integer experimentCount;
    private Integer datasetCount;
    private Integer markersCount;
    private Integer dnaRunsCount;
    private List<EntityPropertyDTO> properties = new java.util.ArrayList<>();

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() {
        return projectId;
    }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @GobiiEntityParam(paramName = "projectName")
    public String getProjectName() {
        return projectName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @GobiiEntityParam(paramName = "projectDescription")
    public String getProjectDescription() {
        return projectDescription;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @GobiiEntityParam(paramName = "piContactId")
    public Integer getPiContactId() {
        return piContactId;
    }

    @GobiiEntityColumn(columnName = "pi_contact")
    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    @GobiiEntityParam(paramName="piContactName")
    public String getPiContactName() {
        return piContactName;
    }

    public void setPiContactName(String piContactName) {
        this.piContactName = piContactName;
    }

    @GobiiEntityParam(paramName="experimentCount")
    public Integer getExperimentCount() {
        return experimentCount;
    }

    public void setExperimentCount(Integer experimentCount) {
        this.experimentCount = experimentCount;
    }

    @GobiiEntityParam(paramName="datasetCount")
    public Integer getDatasetCount() {
        return datasetCount;
    }

    public void setDatasetCount(Integer datasetCount) {
        this.datasetCount = datasetCount;
    }

    @GobiiEntityParam(paramName="markersCount")
    public Integer getMarkersCount() {
        return markersCount;
    }

    public void setMarkersCount(Integer markersCount) {
        this.markersCount = markersCount;
    }


    @GobiiEntityParam(paramName="dnaRunsCount")
    public Integer getDnaRunsCount() {
        return dnaRunsCount;
    }

    public void setDnaRunsCount(Integer dnaRunsCount) {
        this.dnaRunsCount = dnaRunsCount;
    }

    public List<EntityPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<EntityPropertyDTO> properties) {
        this.properties = properties;
    }
}
