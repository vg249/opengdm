package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//Ignore if value is missed
@JsonInclude(JsonInclude.Include.NON_NULL)
// Ignore unknown values defined in JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionUnit {

    // Name of the column to compare
    @JsonProperty
    public String columnName;
    // Is this column a required field or not
    @JsonProperty
    public String required;
    // Is this field unique
    @JsonProperty
    public String nullAllowed;
    @JsonProperty
    public String unique;
    // Is the comparison a file or table in DB
    @JsonProperty
    public List<String> uniqueColumns;
    @JsonProperty
    public String type;
    // Name of file or DB
    @JsonProperty
    public String typeName;
    // Field to compare against in DB or other file
    @JsonProperty
    public String fieldToCompare;
}