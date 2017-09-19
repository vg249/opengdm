package org.gobiiproject.gobiimodel.CvNames;

/**
 * Created by Phil on 5/13/2016.
 */


public enum CvGroup {

    CVGROUP_JOBTYPE("job_type");

    private String cvGroupName;

    CvGroup(String cvGroupName) {
        this.cvGroupName = cvGroupName;
    }

    public String getCvGroupName() {
        return this.cvGroupName;
    }

}
