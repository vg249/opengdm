package org.gobiiproject.gobiidao.entities;
// Generated Mar 29, 2016 12:17:37 PM by Hibernate Tools 3.2.2.GA


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MapProp generated by hbm2java
 */
@Entity
@Table(name="map_prop"
    ,schema="public"
)
public class MapProp  implements java.io.Serializable {


     private int mapPropId;
     private int mapId;
     private Serializable props;

    public MapProp() {
    }

	
    public MapProp(int mapPropId, int mapId) {
        this.mapPropId = mapPropId;
        this.mapId = mapId;
    }
    public MapProp(int mapPropId, int mapId, Serializable props) {
       this.mapPropId = mapPropId;
       this.mapId = mapId;
       this.props = props;
    }
   
     @Id 
    
    @Column(name="map_prop_id", unique=true, nullable=false)
    public int getMapPropId() {
        return this.mapPropId;
    }
    
    public void setMapPropId(int mapPropId) {
        this.mapPropId = mapPropId;
    }
    
    @Column(name="map_id", nullable=false)
    public int getMapId() {
        return this.mapId;
    }
    
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    
    @Column(name="props")
    public Serializable getProps() {
        return this.props;
    }
    
    public void setProps(Serializable props) {
        this.props = props;
    }




}


