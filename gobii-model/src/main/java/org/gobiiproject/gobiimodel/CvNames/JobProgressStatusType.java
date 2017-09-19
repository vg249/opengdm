package org.gobiiproject.gobiimodel.CvNames;

/**
 * Created by Phil on 5/13/2016.
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


}
