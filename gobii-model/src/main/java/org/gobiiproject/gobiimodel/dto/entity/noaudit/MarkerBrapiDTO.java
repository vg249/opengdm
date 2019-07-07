package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

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
    private Integer variantSetDbId;
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

    @GobiiEntityParam(paramName = "variantSetDbId")
    public Integer getVariantSetDbId() { return this.variantSetDbId; }

    @GobiiEntityColumn(columnName = "dataset_ids")
    public void setVariantSetDbId(Integer variantSetDbId) { this.variantSetDbId = variantSetDbId; }

    @GobiiEntityParam(paramName = "variantType")
    public String getVariantType() { return this.variantType; }

    @GobiiEntityColumn(columnName = "variant_type")
    public void setVariantType(String variantType) { this.variantType = variantType; }

    @GobiiEntityParam(paramName = "referenceName")
    public String getReferenceName() { return this.referenceName; }

    @GobiiEntityColumn(columnName = "reference_name")
    public void setReferenceName(String referenceName) { this.referenceName = referenceName; }

}
