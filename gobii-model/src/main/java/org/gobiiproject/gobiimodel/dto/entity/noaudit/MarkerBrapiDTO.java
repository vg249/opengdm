package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 7/3/2019.
 */


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarkerBrapiDTO extends DTOBase {

    private int variantDbId;
    private String variantName;
    private List<Integer> variantSetDbIds = new ArrayList<>();
    private String variantType;
    private String referenceName;

    @Override
    public Integer getId() { return this.variantDbId; }

    @Override
    public void setId(Integer id) { this.variantDbId = id; }

    @GobiiEntityParam(paramName = "variantDbId")
    public Integer getVariantDbId() { return this.variantDbId; }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setVariantDbId(Integer variantDbId) { this.variantDbId = variantDbId; }

    @GobiiEntityParam(paramName = "variantName")
    public String getVariantName() { return this.variantName; }

    @GobiiEntityColumn(columnName = "name")
    public void setVariantName(String variantName) { this.variantName = variantName; }

    @GobiiEntityParam(paramName = "variantSetDbIds")
    public List<Integer> getVariantSetDbId() { return this.variantSetDbIds; }

    @GobiiEntityColumn(columnName = "dataset_ids")
    public void setVariantSetDbId(List<Integer> variantSetDbIds) { this.variantSetDbIds = variantSetDbIds; }

    @GobiiEntityParam(paramName = "variantType")
    public String getVariantType() { return this.variantType; }

    @GobiiEntityColumn(columnName = "variant_type")
    public void setVariantType(String variantType) { this.variantType = variantType; }

    @GobiiEntityParam(paramName = "referenceName")
    public String getReferenceName() { return this.referenceName; }

    @GobiiEntityColumn(columnName = "reference_name")
    public void setReferenceName(String referenceName) { this.referenceName = referenceName; }

}
