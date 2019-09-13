package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;

/**
 * Model for Project Entity.
 * Represents the database table project.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "dnasample")
public class DnaSample extends BaseEntity {

    @Id
    @Column(name="dnasample_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer dnaSampleId;

    @Column(name="name")
    public String dnaSampleName;


    @Column(name="uuid")
    public String dnaSampleUuid;

    @Column(name="code")
    public Integer dnaSampleCode;

    @Column(name="platename")
    public String plateName;

    @Column(name="num")
    public String dnaSampleNum;

    @Column(name="well_row")
    public String wellRow;

    @Column(name="well_col")
    public String wellCol;

    @Column(name="project_id")
    public String projectId;

    @Column(name="germplasm_id")
    public String germplasmId;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    public JsonNode properties;

    public Integer getDnaSampleId() {
        return dnaSampleId;
    }

    public void setDnaSampleId(Integer dnaSampleId) {
        this.dnaSampleId = dnaSampleId;
    }

    public String getDnaSampleName() {
        return dnaSampleName;
    }

    public void setDnaSampleName(String dnaSampleName) {
        this.dnaSampleName = dnaSampleName;
    }

    public Integer getDnaSampleCode() {
        return dnaSampleCode;
    }

    public void setDnaSampleCode(Integer dnaSampleCode) {
        this.dnaSampleCode = dnaSampleCode;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getDnaSampleNum() {
        return dnaSampleNum;
    }

    public void setDnaSampleNum(String dnaSampleNum) {
        this.dnaSampleNum = dnaSampleNum;
    }

    public String getWellRow() {
        return wellRow;
    }

    public void setWellRow(String wellRow) {
        this.wellRow = wellRow;
    }

    public String getWellCol() {
        return wellCol;
    }

    public void setWellCol(String wellCol) {
        this.wellCol = wellCol;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getGermplasmId() {
        return germplasmId;
    }

    public void setGermplasmId(String germplasmId) {
        this.germplasmId = germplasmId;
    }

    public JsonNode getProperties() {
        return properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public String getDnaSampleUuid() {
        return dnaSampleUuid;
    }

    public void setDnaSampleUuid(String dnaSampleUuid) {
        this.dnaSampleUuid = dnaSampleUuid;
    }
}
