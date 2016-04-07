package org.gobiiproject.gobiimodel.dto.container;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectDTO {

    private List<String> principleInvestigators = null;

    public List<String> getPrincipleInvestigators() {
        return principleInvestigators;
    }

    public void setPrincipleInvestigators(List<String> principleInvestigators) {
        this.principleInvestigators = principleInvestigators;
    }
}
