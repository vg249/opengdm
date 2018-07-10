package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//Ignore if value is missed
@JsonInclude(JsonInclude.Include.NON_NULL)
// Ignore unknown values defined in JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionUnit {

    // Name of the column to compare
    private String columnName;
    // Is this column a required field or not
    private boolean required;
    // Does this field allows duplicates or not
    private boolean allowDuplicates;
    // Is the comparison a file or table in DB
    private String type;
    // Name of file or DB
    private String typeName;
    // Field to compare against in DB or other file
    private String fieldToCompare;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFieldToCompare() {
        return fieldToCompare;
    }

    public void setFieldToCompare(String fieldToCompare) {
        this.fieldToCompare = fieldToCompare;
    }
}