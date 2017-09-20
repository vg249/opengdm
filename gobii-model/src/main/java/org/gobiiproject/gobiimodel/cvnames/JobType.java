package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 */


public enum JobType {

    CV_JOBTYPE_LOAD("load"),
    CV_JOBTYPE_EXTRACT("extract"),
    CV_JOBTYPE_ANALYSIS("analysis");

    private String jobTypeCvName;

    JobType(String jobTypeCvName) {
        this.jobTypeCvName = jobTypeCvName;
    }

    public String getCvName() {
        return this.jobTypeCvName;
    }


}
