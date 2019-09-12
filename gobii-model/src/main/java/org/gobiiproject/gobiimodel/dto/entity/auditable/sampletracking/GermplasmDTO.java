package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 5/3/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType", "id"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GermplasmDTO extends DTOBaseAuditable {

    private int id;
    private String germplasmName;
    private String externalCode;
    private String germplasmSpecies;
    private String germplasmType;

    private Map<String, String> properties = new HashMap<>();

    public GermplasmDTO() { super(GobiiEntityNameType.GERMPLASM); }

    @Override
    @GobiiEntityParam(paramName = "germplasmId")
    public Integer getId() { return this.id; }

    @Override
    @GobiiEntityColumn(columnName = "germplasm_id")
    public void setId(Integer id) { this.id = id; }

    public Integer getGermplasmId() { return this.id; }

    public void setGermplasmId(Integer id) { this.id = id; }

    @GobiiEntityParam(paramName = "name")
    public String getGermplasmName() {
        return this.germplasmName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setGermplasmName(String name) {
        this.germplasmName = name;
    }

    @GobiiEntityParam(paramName = "externalCode")
    public String getExternalCode() {
        return this.externalCode;
    }

    @GobiiEntityColumn(columnName = "external_code")
    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    @GobiiEntityParam(paramName = "species_id")
    public String getGermplasmSpecies() {
        return this.germplasmSpecies;
    }

    @GobiiEntityColumn(columnName = "species_id")
    public void setGermplasmSpecies(String speciesName) {
        this.germplasmSpecies = speciesName;
    }

    @GobiiEntityParam(paramName = "type_id")
    public String getGermplasmType() {
        return this.germplasmType;
    }

    @GobiiEntityColumn(columnName = "type_id")
    public void setGermplasmType(String typeName) {
        this.germplasmType = typeName;
    }
}
