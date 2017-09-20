package org.gobiiproject.gobiimodel.dto.instructions.loader;


import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.entity.PropNameId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A loader instruction containing all the details nessisary to create a digest file.
 * See {@link GobiiFile} and {@link GobiiFileColumn}
 * Created by Phil on 4/12/2016.
 */
public class GobiiLoaderInstruction {




    //Tables collection
    Map<String,List<GobiiFileColumn>> columnsByTableName = new HashMap<>();

    //Type of load
    private JobPayloadType jobPayloadType;
    //File location information (Each table can come from a separate file)
    private GobiiFile gobiiFile = new GobiiFile();
    //Name of this table. Used as filename for loading, and to determine what database table it goes to.
    private String table = null;
    //List of GobiiFileColumn columns, left to right ordering
    private List<GobiiFileColumn> gobiiFileColumns = new ArrayList<>();
    //Special filtering parameters for VCF (Mostly ignored)
    private VcfParameters vcfParameters = new VcfParameters();
    //ID of the dataset, used when loading matrix data
    private Integer dataSetId;
    //Name of the crop being loaded
    private String gobiiCropType;
    //ID of the primary contact for this action
    private Integer contactId;
    //Email of the primary contact for this action
    private String contactEmail;

    private boolean qcCheck;

    private PropNameId project = new PropNameId();
    private PropNameId platform = new PropNameId();
    private PropNameId dataSet = new PropNameId();
    private PropNameId datasetType = new PropNameId();
    private PropNameId Experiment = new PropNameId();
    private PropNameId mapset = new PropNameId();


    public Map<String, List<GobiiFileColumn>> getColumnsByTableName() {
        return columnsByTableName;
    }

    public void setColumnsByTableName(Map<String, List<GobiiFileColumn>> columnsByTableName) {
        this.columnsByTableName = columnsByTableName;
    }

    public GobiiFile getGobiiFile() {
        return gobiiFile;
    }

    public GobiiLoaderInstruction setGobiiFile(GobiiFile gobiiFile) {

        this.gobiiFile = gobiiFile;
        return this;
    }


    public JobPayloadType getJobPayloadType() {
        return jobPayloadType;
    }

    public void setJobPayloadType(JobPayloadType jobPayloadType) {
        this.jobPayloadType = jobPayloadType;
    }

    public String getTable() {
        return table;
    }

    public GobiiLoaderInstruction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<GobiiFileColumn> getGobiiFileColumns() {
        return gobiiFileColumns;
    }

    public GobiiLoaderInstruction setGobiiFileColumns(List<GobiiFileColumn> gobiiFileColumns) {
        this.gobiiFileColumns = gobiiFileColumns;
        return this;
    }

    public VcfParameters getVcfParameters() {
        return vcfParameters;
    }

    public GobiiLoaderInstruction setVcfParameters(VcfParameters vcfParameters) {
        this.vcfParameters = vcfParameters;
        return this;
    }

    public Integer getDataSetId() {
        return dataSetId;
    }

    public GobiiLoaderInstruction setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiLoaderInstruction setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public Integer getContactId() {
        return contactId;
    }

    public GobiiLoaderInstruction setContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public GobiiLoaderInstruction setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public boolean isQcCheck() {
        return qcCheck;
    }

    public void setQcCheck(boolean qcCheck) {
        this.qcCheck = qcCheck;
    }

    public PropNameId getProject() {
        return project;
    }

    public void setProject(PropNameId project) {
        this.project = project;
    }

    public PropNameId getPlatform() {
        return platform;
    }

    public void setPlatform(PropNameId platform) {
        this.platform = platform;
    }

    public PropNameId getDataSet() {
        return dataSet;
    }

    public void setDataSet(PropNameId dataSet) {
        this.dataSet = dataSet;
    }

    public PropNameId getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(PropNameId datasetType) {
        this.datasetType = datasetType;
    }

    public PropNameId getExperiment() {
        return Experiment;
    }

    public void setExperiment(PropNameId experiment) {
        Experiment = experiment;
    }

    public PropNameId getMapset() {
        return mapset;
    }

    public void setMapset(PropNameId mapset) {
        this.mapset = mapset;
    }
}
