package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectDTO extends DtoMetaData {

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private int projectId;
    private String projectName;
    private String projectCode;
    private String projectDescription;
    private int piContact;
    private Map<String,String> principleInvestigators = null;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getPiContact() {
        return piContact;
    }

    public void setPiContact(int piContact) {
        this.piContact = piContact;
    }


    public Map<String,String> getPrincipleInvestigators() {
        return principleInvestigators;
    }

    public void setPrincipleInvestigators(Map<String,String> principleInvestigators) {
        this.principleInvestigators = principleInvestigators;
    }




}
