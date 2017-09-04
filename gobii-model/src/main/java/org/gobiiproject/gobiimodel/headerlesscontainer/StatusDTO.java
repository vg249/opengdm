package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;

/**
 * Created by VCalaminos on 8/25/2017.
 */
public class StatusDTO extends DTOBase {

    public StatusDTO() {}

    @Override
    public Integer getId() { return this.jobId; }

    @Override
    public void setId(Integer id) { this.jobId = id; }


    // The values must correspond to the CV term seed values, which 
    // are also hard-coded
    public static final String CVGROUP_JOBTYPE = "job_type";
    public static final String CV_PROGRESSSTATUS_PENDING = "pending";
    public static final String CV_PROGRESSSTATUS_STARTED = "started";
    public static final String CV_PROGRESSSTATUS_INPROGRESS = "in_progress";
    public static final String CV_PROGRESSSTATUS_COMPLETED = "completed";
    public static final String CV_PROGRESSSTATUS_FAILED = "failed";

    public static final String CVGROUP_PAYLOADTYPE = "payload_type";
    public static final String CV_PAYLOADTYPE_SAMPLES = "samples";
    public static final String CV_PAYLOADTYPE_MARKERS = "markers";
    public static final String CV_PAYLOADTYPE_MATRIX = "matrix";
    public static final String CV_PAYLOADTYPE_MARKERSAMPLES = "marker_samples";
    public static final String CV_PAYLOADTYPE_ALLMETA = "all_meta";

    public static final String CVGROUP_JOBSTATUS = "job_status";
    public static final String CV_JOBTYPE_LOAD = "load";
    public static final String CV_JOBTYPE_EXTRACT = "extract";
    public static final String CV_JOBTYPE_ANALYSIS = "analysis";

    private Integer jobId;
    private Integer typeId;
    private Integer payloadTypeId;
    private Integer status;
    private String message;
    private Integer submittedBy;
    private Date submittedDate;

    @GobiiEntityParam(paramName = "jobId")
    public Integer getJobId() { return jobId; }

    @GobiiEntityColumn(columnName = "job_id")
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    @GobiiEntityParam(paramName = "typeId")
    public Integer getTypeId() { return typeId; }

    @GobiiEntityColumn(columnName = "type_id")
    public void setTypeId(Integer typeId) { this.typeId = typeId;}

    @GobiiEntityParam(paramName = "payloadTypeId")
    public Integer getPayloadTypeId() { return payloadTypeId; }

    @GobiiEntityColumn(columnName = "payload_type_id")
    public void setPayloadTypeId(Integer payloadTypeId) { this.payloadTypeId = payloadTypeId; }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() { return status; }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) { this.status = status; }

    @GobiiEntityParam(paramName = "message")
    public String getMessage(){ return message; }

    @GobiiEntityColumn(columnName = "message")
    public void setMessage(String message) { this.message = message; }

    @GobiiEntityParam(paramName = "submittedBy")
    public Integer getSubmittedBy() { return submittedBy; }

    @GobiiEntityColumn(columnName = "submitted_by")
    public void setSubmittedBy(Integer submittedBy) { this.submittedBy = submittedBy; }

    @GobiiEntityParam(paramName = "submittedDate")
    public Date getSubmittedDate() { return submittedDate; }

    @GobiiEntityColumn(columnName = "submitted_date")
    public void setSubmittedDate(Date submittedDate) { this.submittedDate = submittedDate; }

}
