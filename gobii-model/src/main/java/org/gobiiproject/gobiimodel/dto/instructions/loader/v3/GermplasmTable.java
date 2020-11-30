package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "germplasm")
public class GermplasmTable implements Table {

    @JsonProperty("name")
    private Aspect germplasmName;

    @JsonProperty("name")
    private Aspect germplasmExternalCode;

    @JsonProperty("type_name")
    private Aspect germplasmType;

    @JsonProperty("species_name")
    private Aspect germplasmSpeciesName;

    private String status;

    @JsonProperty("props")
    private JsonAspect germplasmProperties;

    public Aspect getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(Aspect germplasmName) {
        this.germplasmName = germplasmName;
    }

    public Aspect getGermplasmExternalCode() {
        return germplasmExternalCode;
    }

    public void setGermplasmExternalCode(Aspect germplasmExternalCode) {
        this.germplasmExternalCode = germplasmExternalCode;
    }

    public Aspect getGermplasmType() {
        return germplasmType;
    }

    public void setGermplasmType(Aspect germplasmType) {
        this.germplasmType = germplasmType;
    }

    public Aspect getGermplasmSpeciesName() {
        return germplasmSpeciesName;
    }

    public void setGermplasmSpeciesName(Aspect germplasmSpeciesName) {
        this.germplasmSpeciesName = germplasmSpeciesName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonAspect getGermplasmProperties() {
        return germplasmProperties;
    }

    public void setGermplasmProperties(JsonAspect germplasmProperties) {
        this.germplasmProperties = germplasmProperties;
    }
}
