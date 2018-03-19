package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseAlleleMatricesItem {


    private String name;
    private String matrixDbId;
    private String description;
    private String lastUpdated;
    private String studyDbId;
    private Integer totalMarkers;
    private Integer totalSamples;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatrixDbId() {
        return matrixDbId;
    }

    public void setMatrixDbId(String matrixDbId) {
        this.matrixDbId = matrixDbId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public Integer getTotalMarkers() { return totalMarkers; }

    public void setTotalMarkers(Integer totalMarkers) { this.totalMarkers = totalMarkers; }

    public Integer getTotalSamples() { return totalSamples; }

    public void setTotalSamples(Integer totalSamples) { this.totalSamples = totalSamples; }
}
