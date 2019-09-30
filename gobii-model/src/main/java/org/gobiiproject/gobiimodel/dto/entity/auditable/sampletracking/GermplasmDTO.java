package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Germplasm;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.HashMap;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType", "id"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GermplasmDTO extends DTOBaseAuditable {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GobiiEntityMap(paramName = "germplasmId", entity = Germplasm.class)
    private int germplasmId;

    @GobiiEntityMap(paramName = "germplasmName", entity = Germplasm.class)
    private String germplasmName;

    @GobiiEntityMap(paramName = "externalCode", entity = Germplasm.class)
    private String externalCode;

    private String germplasmSpecies;

    private String germplasmType;

    private Map<String, String> properties = new HashMap<>();

    public GermplasmDTO() { super(GobiiEntityNameType.GERMPLASM); }

    @Override
    public Integer getId() { return this.germplasmId; }

    @Override
    public void setId(Integer id) { this.germplasmId = id; }

    public Integer getGermplasmId() { return this.germplasmId; }

    public void setGermplasmId(Integer id) { this.germplasmId = id; }

    public String getGermplasmName() {
        return this.germplasmName;
    }

    public void setGermplasmName(String name) {
        this.germplasmName = name;
    }

    public String getExternalCode() {
        return this.externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getGermplasmSpecies() {
        return this.germplasmSpecies;
    }

    public void setGermplasmSpecies(String speciesName) {
        this.germplasmSpecies = speciesName;
    }

    public String getGermplasmType() {
        return this.germplasmType;
    }

    public void setGermplasmType(String typeName) {
        this.germplasmType = typeName;
    }
}
