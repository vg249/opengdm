package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by VCalaminos on 5/3/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GermplasmDTO extends DTOBaseAuditable {

    private int id;
    private String name;
    private String externalCode;

    public GermplasmDTO() { super(GobiiEntityNameType.GERMPLASM); }

    @Override
    @GobiiEntityParam(paramName = "germplasmId")
    public Integer getId() { return this.id; }

    @Override
    @GobiiEntityColumn(columnName = "germplasm_id")
    public void setId(Integer id) { this.id = id; }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return this.name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "externalCode")
    public String getExternalCode() {
        return this.externalCode;
    }

    @GobiiEntityColumn(columnName = "external_code")
    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }
}
