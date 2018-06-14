package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VertexColumns {


    // this works for almost all cases because the name of the ID field
    // will always be "id" even when the actual entity (e.g., dataset) has
    // a different name (e.g., dataset_id)
    // The only cases in which we will need a different implementation
    // is when we need to capture different fields in addition to name, such as
    // with principle investigator
    private final String FIELD_ID = "id";


    private List<String> columns;

    public VertexColumns(List<String> entityColumns) {

        this.columns = new ArrayList<>();
        this.columns.add(FIELD_ID);
        this.columns.addAll(entityColumns);
    }

    @Override
    public List<String> getColumnNames() {
        return columns;
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        NameIdDTO returnVal = new NameIdDTO();

        String[] values = line.split("\t", -1);

        String id = values[this.columns.indexOf(FIELD_ID)];
        String name = values[this.columns.indexOf(FIELD_NAME)];

        returnVal.setName(name);
        returnVal.setId(Integer.parseInt(id));

        return returnVal;

    }
}
