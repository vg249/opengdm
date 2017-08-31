package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

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
    public static final Integer CV_PROGRESSSTATUS_PENDING = 5;
    public static final Integer CV_PROGRESSSTATUS_STARTED = 6;
    public static final Integer CV_PROGRESSSTATUS_INPROGRESS = 7;
    public static final Integer CV_PROGRESSSTATUS_COMPLETED = 8;
    public static final Integer CV_PROGRESSSTATUS_FAILED = 9;

    public static final Integer CV_LOADTYPE_MATRIX = 6;
    public static final Integer CV_LOADTYPE_SAMPLE = 7;
    public static final Integer CV_LOADTYPE_MARKER = 8;


    public static final Integer CV_EXTRACTTYPE_HAPMAP = 6;
    public static final Integer CV_EXTRACTTYPE_FLAPJACK = 7;
    public static final Integer CV_EXTRACTTYPE_QTL = 8;

    private Integer jobId;
    private Integer processStatus;
    private String messages;
    private Integer loadType;
    private Integer extractType;
    private Integer submittedBy;
    private Integer dataset;

    @GobiiEntityParam(paramName = "jobId")
    public Integer getJobId() { return jobId; }

    @GobiiEntityColumn(columnName = "job_id")
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    @GobiiEntityParam(paramName = "processStatus")
    public Integer getProcessStatus() { return processStatus; }

    @GobiiEntityColumn(columnName = "process_status")
    public void setProcessStatus(Integer processStatus) { this.processStatus = processStatus; }

    @GobiiEntityParam(paramName = "messages")
    public String getMessages(){ return messages; }

    @GobiiEntityColumn(columnName = "messages")
    public void setMessages(String messages) { this.messages = messages; }

    @GobiiEntityParam(paramName = "loadType")
    public Integer getLoadType() { return loadType; }

    @GobiiEntityColumn(columnName = "load_type")
    public void setLoadType(Integer loadType) { this.loadType = loadType; }

    @GobiiEntityParam(paramName = "extractType")
    public Integer getExtractType() { return extractType; }

    @GobiiEntityColumn(columnName = "extract_type")
    public void setExtractType(Integer extractType) { this.extractType = extractType; }

    @GobiiEntityParam(paramName = "submittedBy")
    public Integer getSubmittedBy() { return submittedBy; }

    @GobiiEntityColumn(columnName = "submitted_by")
    public void setSubmittedBy(Integer submittedBy) { this.submittedBy = submittedBy; }

    @GobiiEntityParam(paramName = "dataset")
    public Integer getDataset() { return dataset; }

    @GobiiEntityColumn(columnName = "dataset")
    public void setDataset(Integer dataset) { this.dataset = dataset; }


}
