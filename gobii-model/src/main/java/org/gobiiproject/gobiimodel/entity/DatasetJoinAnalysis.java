package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

public class DatasetJoinAnalysis extends Dataset{

    @Transient
    private Set<Analysis> transientAnalysesSet = new HashSet<>();

    public Set<Analysis> getTransientAnalysesSet() {
        return transientAnalysesSet;
    }

    public void setTransientAnalysesSet(Set<Analysis> transientAnalysesSet) {
        this.transientAnalysesSet = transientAnalysesSet;
    }
}
