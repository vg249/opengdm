package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class Vertex {


    private Integer vertexId = 0;
    private List<Integer> filterVals = new ArrayList<>();


    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public List<Integer> getFilterVals() {
        return filterVals;
    }

    public void setFilterVals(List<Integer> filterVals) {
        this.filterVals = filterVals;
    }
}
