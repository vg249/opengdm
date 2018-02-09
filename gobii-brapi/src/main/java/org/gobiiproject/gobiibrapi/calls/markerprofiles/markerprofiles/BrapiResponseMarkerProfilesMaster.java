package org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles;

import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseMarkerProfilesMaster extends BrapiResponseDataList<BrapiResponseMarkerProfileDetail> {


    public BrapiResponseMarkerProfilesMaster() {
    }

    String germplasmDbId;
    String uniqueDisplayName;
    String extractDbId;
    String markerprofileDbId;
    String analysisMethod;

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getUniqueDisplayName() {
        return uniqueDisplayName;
    }

    public void setUniqueDisplayName(String uniqueDisplayName) {
        this.uniqueDisplayName = uniqueDisplayName;
    }

    public String getExtractDbId() {
        return extractDbId;
    }

    public void setExtractDbId(String extractDbId) {
        this.extractDbId = extractDbId;
    }

    public String getMarkerprofileDbId() {
        return markerprofileDbId;
    }

    public void setMarkerprofileDbId(String markerprofileDbId) {
        this.markerprofileDbId = markerprofileDbId;
    }

    public String getAnalysisMethod() {
        return analysisMethod;
    }

    public void setAnalysisMethod(String analysisMethod) {
        this.analysisMethod = analysisMethod;
    }
}
