package org.gobiiproject.gobiimodel.dto.instructions.loader;


import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class GobiiLoaderInstruction {

    private GobiiFile gobiiFile = new GobiiFile();
    private String table = null;
    private List<GobiiFileColumn> gobiiFileColumns = new ArrayList<>();
    private VcfParameters vcfParameters = new VcfParameters();
    private Integer dataSetId;
    private String gobiiCropType;
    Integer contactId;
    String contactEmail;

    private GobiiFilePropNameId project = new GobiiFilePropNameId();
    private GobiiFilePropNameId platform = new GobiiFilePropNameId();
    private GobiiFilePropNameId dataSet = new GobiiFilePropNameId();
    private GobiiFilePropNameId datasetType = new GobiiFilePropNameId();
    private GobiiFilePropNameId Experiment = new GobiiFilePropNameId();
    private GobiiFilePropNameId mapset = new GobiiFilePropNameId();

    public GobiiFile getGobiiFile() {
        return gobiiFile;
    }

    public GobiiLoaderInstruction setGobiiFile(GobiiFile gobiiFile) {

        this.gobiiFile = gobiiFile;
        return this;
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

    public GobiiFilePropNameId getProject() {
        return project;
    }

    public void setProject(GobiiFilePropNameId project) {
        this.project = project;
    }

    public GobiiFilePropNameId getPlatform() {
        return platform;
    }

    public void setPlatform(GobiiFilePropNameId platform) {
        this.platform = platform;
    }

    public GobiiFilePropNameId getDataSet() {
        return dataSet;
    }

    public void setDataSet(GobiiFilePropNameId dataSet) {
        this.dataSet = dataSet;
    }

    public GobiiFilePropNameId getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(GobiiFilePropNameId datasetType) {
        this.datasetType = datasetType;
    }

    public GobiiFilePropNameId getExperiment() {
        return Experiment;
    }

    public void setExperiment(GobiiFilePropNameId experiment) {
        Experiment = experiment;
    }

    public GobiiFilePropNameId getMapset() {
        return mapset;
    }

    public void setMapset(GobiiFilePropNameId mapset) {
        this.mapset = mapset;
    }
}
