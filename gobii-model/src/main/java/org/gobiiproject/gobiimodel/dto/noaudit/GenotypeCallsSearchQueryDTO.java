package org.gobiiproject.gobiimodel.dto.noaudit;

import org.gobiiproject.gobiimodel.validators.CheckAtLeastOneNotNullOrEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@CheckAtLeastOneNotNullOrEmpty(fieldNames = {"callSetDbIds"})
public class GenotypeCallsSearchQueryDTO {

    @Size(max = 1000, message = "Only 1000 callSetIds allowed per query")
    private List<Integer> callSetDbIds;

    @Size(max = 1000, message = "Only 1000 callSetNames allowed per query")
    private List<String> callSetNames;

    @Size(max = 1000, message = "Only 1000 variantDbIds allowed per query")
    private List<Integer> varinatDbIds;

    @Size(max = 1000, message = "Only 1000 variantNames allowed per query")
    private List<String> variantNames;

    @Size(max = 1000, message = "Only 1000 variantSetDbIds allowed per query")
    private List<Integer> variantSetDbIds;

    @Size(max = 1000, message = "Only 1000 variantSetNames allowed per query")
    private List<String> variantSetNames;

    @Size(max = 1000, message = "Only 1000 germplasmPUIs allowed per query")
    private List<String> germplasmPUIs;

    public List<Integer> getCallSetDbIds() {
        return callSetDbIds;
    }

    public void setCallSetDbIds(List<Integer> callSetDbIds) {
        this.callSetDbIds = callSetDbIds;
    }

    public List<String> getCallSetNames() {
        return callSetNames;
    }

    public void setCallSetNames(List<String> callSetNames) {
        this.callSetNames = callSetNames;
    }

    public List<Integer> getVarinatDbIds() {
        return varinatDbIds;
    }

    public void setVarinatDbIds(List<Integer> varinatDbIds) {
        this.varinatDbIds = varinatDbIds;
    }

    public List<String> getVariantNames() {
        return variantNames;
    }

    public void setVariantNames(List<String> variantNames) {
        this.variantNames = variantNames;
    }

    public List<Integer> getVariantSetDbIds() {
        return variantSetDbIds;
    }

    public void setVariantSetDbIds(List<Integer> variantSetDbIds) {
        this.variantSetDbIds = variantSetDbIds;
    }

    public List<String> getVariantSetNames() {
        return variantSetNames;
    }

    public void setVariantSetNames(List<String> variantSetNames) {
        this.variantSetNames = variantSetNames;
    }

    public List<String> getGermplasmPUIs() {
        return germplasmPUIs;
    }

    public void setGermplasmPUIs(List<String> germplasmPUIs) {
        this.germplasmPUIs = germplasmPUIs;
    }
}
