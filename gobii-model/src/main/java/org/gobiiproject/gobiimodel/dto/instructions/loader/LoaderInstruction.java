package org.gobiiproject.gobiimodel.dto.instructions.loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderInstruction {

    private File file = new File();
    private String table = null;
    private List<Column> columns = new ArrayList<>();
    private VcfParameters vcfParameters = new VcfParameters();;

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public VcfParameters getVcfParameters() {
        return vcfParameters;
    }

    public void setVcfParameters(VcfParameters vcfParameters) {
        this.vcfParameters = vcfParameters;
    }
}
