package org.gobiiproject.gobiimodel.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;

@Entity
@Table(name = "cv")
public class Cv {

    @Id
    @Column(name = "cv_id")
    public Integer cvId;

    @Column(name="term")
    public String term;

    @Column(name="definition")
    public String definition;

    @Column(name = "rank")
    public Integer rank;

    @Column(name = "cvgroup_id")
    public Integer cvGroupId;

    @Column(name = "abbreviation")
    public String abbreviation;

    @Column(name = "dbxref_id")
    public Integer dbxrefId;

    @Column(name = "status")
    public String status;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    public JsonNode props;

    public Integer getCvId() {
        return cvId;
    }

    public void setCvId(Integer cvId) {
        this.cvId = cvId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getCvGroupId() {
        return cvGroupId;
    }

    public void setCvGroupId(Integer cvGroupId) {
        this.cvGroupId = cvGroupId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Integer getDbxrefId() {
        return dbxrefId;
    }

    public void setDbxrefId(Integer dbxrefId) {
        this.dbxrefId = dbxrefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonNode getProps() {
        return props;
    }

    public void setProps(JsonNode props) {
        this.props = props;
    }

}
