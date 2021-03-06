package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 *  * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database

 */


public enum JobProgressStatusType {

    CV_PROGRESSSTATUS_PENDING ("pending"),
    CV_PROGRESSSTATUS_INPROGRESS ("in_progress"),
    CV_PROGRESSSTATUS_COMPLETED ("completed"),
    CV_PROGRESSSTATUS_FAILED ("failed"),
    CV_PROGRESSSTATUS_VALIDATION ("validation"),
    CV_PROGRESSSTATUS_DIGEST ("digest"),
    CV_PROGRESSSTATUS_TRANSFORMATION ("transformation"),
    CV_PROGRESSSTATUS_METADATALOAD ("metadata_load"),
    CV_PROGRESSSTATUS_MATRIXLOAD ("matrix_load"),
    CV_PROGRESSSTATUS_ABORTED ("aborted"),
    CV_PROGRESSSTATUS_METADATAEXTRACT ("metadata_extract"),
    CV_PROGRESSSTATUS_FINALASSEMBLY ("final_assembly"),
    CV_PROGRESSSTATUS_QCPROCESSING ("qc_processing"),
    CV_PROGRESSSTATUS_NOSTATUS ("no_status");

    private String progressStatusName;

    JobProgressStatusType(String progressStatusName) {
        this.progressStatusName = progressStatusName;
    }

    public String getCvName() {
        return this.progressStatusName;
    }

    public static JobProgressStatusType byValue(String val) {
        for (JobProgressStatusType en : values()) {
            if (en.getCvName().equals(val)) {
                return en;
            }
        }
        return null;
    }

}
