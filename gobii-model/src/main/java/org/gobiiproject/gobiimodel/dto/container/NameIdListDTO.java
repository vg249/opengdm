package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class NameIdListDTO extends Header {

    public enum EntityType {DBTABLE, CVTERM};
    private EntityType entityType = EntityType.DBTABLE;
    private String entityName = null;
    private List<NameIdDTO> namesById = new ArrayList<>();
    private String filter = null;

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<NameIdDTO> getNamesById() {
        return namesById;
    }

    public void setNamesById(List<NameIdDTO> namesById) {
        this.namesById = namesById;
    }
}
