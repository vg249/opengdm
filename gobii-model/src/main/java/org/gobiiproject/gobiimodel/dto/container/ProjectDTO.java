package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectDTO extends DtoMetaData {

    private int projectId;
    private String projectName;
    private String projectCode;
    private String projectDescription;
    private int piContact;


    private Map<String,String> principleInvestigators = null;

    public Map<String,String> getPrincipleInvestigators() {
        return principleInvestigators;
    }

    public void setPrincipleInvestigators(Map<String,String> principleInvestigators) {
        this.principleInvestigators = principleInvestigators;
    }
}
