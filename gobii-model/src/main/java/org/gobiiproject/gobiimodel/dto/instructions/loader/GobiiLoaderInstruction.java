package org.gobiiproject.gobiimodel.dto.instructions.loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class GobiiLoaderInstruction {

    private GobiiFile gobiiFile = new GobiiFile();
    private String table = null;
    private List<Column> columns = new ArrayList<>();
    private VcfParameters vcfParameters = new VcfParameters();
    ;

    public GobiiFile getGobiiFile() {
        return gobiiFile;
    }

    public GobiiLoaderInstruction setGobiiFile(GobiiFile gobiiFile) {

        this.gobiiFile = gobiiFile;
        return this;
    }

    public String getTable() {
        return table;
    }

    public GobiiLoaderInstruction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public GobiiLoaderInstruction setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public VcfParameters getVcfParameters() {
        return vcfParameters;
    }

    public GobiiLoaderInstruction setVcfParameters(VcfParameters vcfParameters) {
        this.vcfParameters = vcfParameters;
        return this;
    }
}
