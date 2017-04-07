package org.gobiiproject.gobiimodel.headerlesscontainer;

public class QCDataDTO extends DTOBase {

    Long dataSetId;
    String dataFile;
    String directory;
    String qualityFile;
    // It is exclusively used by the KDCompute API to identify any QC job.
    // Do not confuse it with the contact identifier of Gobii
    Long contactId;

    public QCDataDTO() {
    }

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {

    }

    public Long getDataSetId() { return dataSetId; }

    public void setDataSetId(Long dataSetId) { this.dataSetId = dataSetId; }

    public String getDataFile() { return dataFile; }

    public void setDataFile(String dataFile) { this.dataFile = dataFile; }

    public String getDirectory() { return directory; }

    public void setDirectory(String directory) { this.directory = directory; }

    public String getQualityFile() { return qualityFile; }

    public void setQualityFile(String qualityFile) { this.qualityFile = qualityFile; }

    // The QC job identifier used by the KDCompute API
    public Long getContactId() { return contactId; }

    public void setContactId(Long contactId) { this.contactId = contactId; }

}
