package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database
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
