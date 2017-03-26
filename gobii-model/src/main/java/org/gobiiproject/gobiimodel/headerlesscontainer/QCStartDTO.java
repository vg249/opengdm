package org.gobiiproject.gobiimodel.headerlesscontainer;

public class QCStartDTO extends DTOBase {

    Integer jobId;

    public QCStartDTO() {
    }

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {

    }

    public Integer getJobId() { return jobId; }

    public void setJobId(Integer jobId) { this.jobId = jobId; }
}
