package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;

/**
 * Created by VCalaminos on 2017-01-04.
 */
public class DataSetTypeDTO extends DTOBase{

    public DataSetTypeDTO() {
    }

    @Override
    public Integer getId() { return this.dataSetTypeId; }

    @Override
    public void setId(Integer id) { this.dataSetTypeId = id; }

    private String dataSetTypeName;
    private Integer dataSetTypeId = 0;

    @GobiiEntityParam(paramName = "name")
    public String getDataSetTypeName() {return dataSetTypeName;}

    @GobiiEntityColumn(columnName = "name")
    public void setDataSetTypeName(String name){ this.dataSetTypeName = name; }

}
