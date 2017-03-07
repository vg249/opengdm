package org.gobiiproject.gobiimodel.dto.instructions;

public class GobiiQCComplete {

    private Integer datasetId;
    private Integer contactId;
    private String dataFileName;
    private String dataFileDirectory;
    private String qualityFileName;

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String getDataFileDirectory() {
        return dataFileDirectory;
    }

    public void setDataFileDirectory(String dataFileDirectory) {
        this.dataFileDirectory = dataFileDirectory;
    }

    public String getQualityFileName() {
        return qualityFileName;
    }

    public void setQualityFileName(String qualityFileName) {
        this.qualityFileName = qualityFileName;
    }

}
