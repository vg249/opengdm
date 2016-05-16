package org.gobiiproject.gobiimodel.entity;

/**
 * Created by araquel on 5/16/2016.
 */
public class CvItem {

    private Integer cv_id;
    private String term;
    private String definition;
    private Integer rank;


    public Integer getCv_id() {
        return cv_id;
    }

    public void setCv_id(Integer cv_id) {
        this.cv_id = cv_id;
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
}
