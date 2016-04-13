package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DisplayDTO extends DtoMetaData {

    String tableName = null;
    Map<String,String> displayNamesByColumn = new HashMap<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getDisplayNamesByColumn() {
        return displayNamesByColumn;
    }

    public void setDisplayNamesByColumn(Map<String, String> displayNamesByColumn) {
        this.displayNamesByColumn = displayNamesByColumn;
    }
}
