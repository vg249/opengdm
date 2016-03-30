package org.gobiiproject.gobiidao.generated.entities;
// Generated Mar 30, 2016 12:20:14 PM by Hibernate Tools 3.2.2.GA


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DnarunProp generated by hbm2java
 */
@Entity
@Table(name="dnarun_prop"
    ,schema="public"
)
public class DnarunProp  implements java.io.Serializable {


     private int dnarunPropId;
     private int dnarunId;
     private Serializable content;

    public DnarunProp() {
    }

    public DnarunProp(int dnarunPropId, int dnarunId, Serializable content) {
       this.dnarunPropId = dnarunPropId;
       this.dnarunId = dnarunId;
       this.content = content;
    }
   
     @Id 
    
    @Column(name="dnarun_prop_id", unique=true, nullable=false)
    public int getDnarunPropId() {
        return this.dnarunPropId;
    }
    
    public void setDnarunPropId(int dnarunPropId) {
        this.dnarunPropId = dnarunPropId;
    }
    
    @Column(name="dnarun_id", nullable=false)
    public int getDnarunId() {
        return this.dnarunId;
    }
    
    public void setDnarunId(int dnarunId) {
        this.dnarunId = dnarunId;
    }
    
    @Column(name="content", nullable=false)
    public Serializable getContent() {
        return this.content;
    }
    
    public void setContent(Serializable content) {
        this.content = content;
    }




}


