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
    private VcfParameters vcfParameters = new VcfParameters();
    ;

    public File getFile() {
        return file;
    }

    public LoaderInstruction setFile(File file) {

        this.file = file;
        return this;
    }

    public String getTable() {
        return table;
    }

    public LoaderInstruction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public LoaderInstruction  setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public VcfParameters getVcfParameters() {
        return vcfParameters;
    }

    public LoaderInstruction  setVcfParameters(VcfParameters vcfParameters) {
        this.vcfParameters = vcfParameters;
        return this;
    }
}
