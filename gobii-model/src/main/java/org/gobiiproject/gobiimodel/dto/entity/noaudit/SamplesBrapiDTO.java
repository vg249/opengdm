package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import java.util.Map;

public class SamplesBrapiDTO {

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getObservationUnitDbId() {
        return observationUnitDbId;
    }

    public void setObservationUnitDbId(String observationUnitDbId) {
        this.observationUnitDbId = observationUnitDbId;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getTissueType() {
        return tissueType;
    }

    public void setTissueType(String tissueType) {
        this.tissueType = tissueType;
    }

    public String getSampleDbId() {
        return sampleDbId;
    }

    public void setSampleDbId(String sampleDbId) {
        this.sampleDbId = sampleDbId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSamplePUI() {
        return samplePUI;
    }

    public void setSamplePUI(String samplePUI) {
        this.samplePUI = samplePUI;
    }

    public String getSampleBarCode() {
        return sampleBarCode;
    }

    public void setSampleBarCode(String sampleBarCode) {
        this.sampleBarCode = sampleBarCode;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getWell() {
        return well;
    }

    public void setWell(String well) {
        this.well = well;
    }

    public String getSampleGroupDbId() {
        return sampleGroupDbId;
    }

    public void setSampleGroupDbId(String sampleGroupDbId) {
        this.sampleGroupDbId = sampleGroupDbId;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    private String germplasmDbId;

    private String observationUnitDbId;

    private String sampleType = "DNA";

    private String tissueType;

    private String sampleDbId;

    private String sampleName;

    private String samplePUI;

    private String sampleBarCode;

    private String plateName;

    private String row;

    private String column;

    private String well;

    private String sampleGroupDbId;

    private Map<String, String> additionalInfo;

}
