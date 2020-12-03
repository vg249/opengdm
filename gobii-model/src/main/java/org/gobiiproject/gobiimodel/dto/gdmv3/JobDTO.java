package org.gobiiproject.gobiimodel.dto.gdmv3;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Job;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.utils.customserializers.UtcDateSerializer;

import java.util.Date;

public class JobDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    @GobiiEntityMap(paramName = "jobId", entity = Job.class)
    private Integer jobId;

    @GobiiEntityMap(paramName = "jobName", entity = Job.class)
    private String jobName;

    @GobiiEntityMap(paramName = "message", entity = Job.class)
    private String jobMessage;

    @GobiiEntityMap(paramName = "payloadType.term", entity = Job.class, deep = true)
    private String payload;

    @GobiiEntityMap(paramName = "type.term", entity = Job.class, deep = true)
    private String jobType;

    @JsonSerialize(using= UtcDateSerializer.class)
    @GobiiEntityMap(paramName="submittedDate", base = true)
    private Date submittedDate;

    @GobiiEntityMap(paramName="submittedBy.username", entity = Job.class, deep = true)
    private String submittedBy;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }
}
