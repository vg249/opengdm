package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.PropNameId;

import java.util.Date;

/**
 * Created by VCalaminos on 8/25/2017.
 */
public class JobDTO extends DTOBase {

    public JobDTO() {}

    @Override
    public Integer getId() { return this.jobId; }

    @Override
    public void setId(Integer id) { this.jobId = id; }


    // The values must correspond to the CV term seed values, which 
    // are also hard-coded
    public static final String CVGROUP_JOBSTATUS = "job_status";
    public static final String CV_PROGRESSSTATUS_PENDING = "pending";
    public static final String CV_PROGRESSSTATUS_INPROGRESS = "in_progress";
    public static final String CV_PROGRESSSTATUS_COMPLETED = "completed";
    public static final String CV_PROGRESSSTATUS_FAILED = "failed";
    public static final String CV_PROGRESSSTATUS_VALIDATION = "validation";
    public static final String CV_PROGRESSSTATUS_DIGEST = "digest";
    public static final String CV_PROGRESSSTATUS_TRANSFORMATION = "transformation";
    public static final String CV_PROGRESSSTATUS_METADATALOAD = "metadata_load";
    public static final String CV_PROGRESSSTATUS_MATRIXLOAD = "matrix_load";
    public static final String CV_PROGRESSSTATUS_ABORTED = "aborted";
    public static final String CV_PROGRESSSTATUS_METADATAEXTRACT = "metadata_extract";
    public static final String CV_PROGRESSSTATUS_FINALASSEMBLY = "final_assembly";
    public static final String CV_PROGRESSSTATUS_QCPROCESSING = "qc_processing";
    public static final String CV_PROGRESSSTATUS_NOSTATUS = "no_status";

    public static final String CVGROUP_PAYLOADTYPE = "payload_type";
    public static final String CV_PAYLOADTYPE_SAMPLES = "samples";
    public static final String CV_PAYLOADTYPE_MARKERS = "markers";
    public static final String CV_PAYLOADTYPE_MATRIX = "matrix";
    public static final String CV_PAYLOADTYPE_MARKERSAMPLES = "marker_samples";
    public static final String CV_PAYLOADTYPE_ALLMETA = "all_meta";

    public static final String CVGROUP_JOBTYPE = "job_type";
    public static final String CV_JOBTYPE_LOAD = "load";
    public static final String CV_JOBTYPE_EXTRACT = "extract";
    public static final String CV_JOBTYPE_ANALYSIS = "analysis";

    private Integer jobId;
    private String jobName;
    private String type;
    private String payloadType;
    private String status;
    private String message;
    private Integer submittedBy;
    private Date submittedDate;
    private Integer datasetId;

    @GobiiEntityParam(paramName = "jobId")
    public Integer getJobId() { return jobId; }

    @GobiiEntityColumn(columnName = "job_id")
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    @GobiiEntityParam(paramName = "jobName")
    public String getJobName() { return jobName; }

    @GobiiEntityColumn(columnName = "name")
    public void setJobName(String jobName) { this.jobName = jobName; }

    @GobiiEntityParam(paramName = "type")
    public String getType() { return type; }

    @GobiiEntityColumn(columnName = "type_id")
    public void setType(String type) { this.type = type;}

    @GobiiEntityParam(paramName = "payloadType")
    public String getPayloadType() { return payloadType; }

    @GobiiEntityColumn(columnName = "payload_type_id")
    public void setPayloadType(String payloadType) { this.payloadType = payloadType; }

    @GobiiEntityParam(paramName = "status")
    public String getStatus() { return status; }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(String status) { this.status = status; }

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

    @GobiiEntityParam(paramName = "datasetId")
    public Integer getDatasetId() { return datasetId; }

    @GobiiEntityColumn(columnName = "dataset_id")
    public void setDatasetId(Integer datasetId) { this.datasetId = datasetId; }

}
