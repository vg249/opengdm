package org.gobiiproject.gobiidao.entities;
// Generated Mar 29, 2016 12:17:37 PM by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Dnarun generated by hbm2java
 */
@Entity
@Table(name="dnarun"
    ,schema="public"
)
public class Dnarun  implements java.io.Serializable {


     private int dnarunId;
     private int experimentId;
     private int dnasampleId;
     private String name;
     private String code;

    public Dnarun() {
    }

	
    public Dnarun(int dnarunId, int experimentId, int dnasampleId, String code) {
        this.dnarunId = dnarunId;
        this.experimentId = experimentId;
        this.dnasampleId = dnasampleId;
        this.code = code;
    }
    public Dnarun(int dnarunId, int experimentId, int dnasampleId, String name, String code) {
       this.dnarunId = dnarunId;
       this.experimentId = experimentId;
       this.dnasampleId = dnasampleId;
       this.name = name;
       this.code = code;
    }
   
     @Id 
    
    @Column(name="dnarun_id", unique=true, nullable=false)
    public int getDnarunId() {
        return this.dnarunId;
    }
    
    public void setDnarunId(int dnarunId) {
        this.dnarunId = dnarunId;
    }
    
    @Column(name="experiment_id", nullable=false)
    public int getExperimentId() {
        return this.experimentId;
    }
    
    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }
    
    @Column(name="dnasample_id", nullable=false)
    public int getDnasampleId() {
        return this.dnasampleId;
    }
    
    public void setDnasampleId(int dnasampleId) {
        this.dnasampleId = dnasampleId;
    }
    
    @Column(name="name")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="code", nullable=false)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }




}


