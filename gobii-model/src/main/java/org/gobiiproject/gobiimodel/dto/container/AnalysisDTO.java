package org.gobiiproject.gobiimodel.dto.container;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisDTO {
    private int analysisId;
    private String analysisName;
    private String analysisDescription;
    private int anlaysisTypeId;
    private String program;
    private String programversion;
    private String algorithm;
    private String sourcename;
    private String sourceversion;
    private String sourceuri;
    private Integer referenceId;
    private Serializable parameters;
    private Date timeexecuted;
    private int status;

}
