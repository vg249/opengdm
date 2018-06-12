package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VertexColumnsNameIdGeneric implements VertexColumns {


    private List columns;
    private String idColumnName;
    private String nameColumnName;
    public VertexColumnsNameIdGeneric(String idColumnName, String nameColumnName) {

        this.idColumnName = idColumnName;
        this.nameColumnName = nameColumnName;
        this.columns = new ArrayList<>(Arrays.asList(
                this.idColumnName,
                this.nameColumnName));
    }

    @Override
    public List<String> getColumnNames() {
        return columns;
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        NameIdDTO returnVal = new NameIdDTO();

        String[] values = line.split("\t", -1);

        String name = values[this.columns.indexOf(this.nameColumnName)];
        String id = values[this.columns.indexOf(this.idColumnName)];

        returnVal.setName(name);
        returnVal.setId( Integer.parseInt(id));

        return returnVal;

    }
}
