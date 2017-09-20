package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 */


public enum JobPayloadType {

    CV_PAYLOADTYPE_SAMPLES ("samples"),
    CV_PAYLOADTYPE_MARKERS ("markers"),
    CV_PAYLOADTYPE_MATRIX ("matrix"),
    CV_PAYLOADTYPE_MARKERSAMPLES ("marker_samples"),
    CV_PAYLOADTYPE_ALLMETA ("all_meta");

    private String cvName;

    JobPayloadType(String cvName) {
        this.cvName = cvName;
    }

    public String getCvName() {
        return this.cvName;
    }


}
