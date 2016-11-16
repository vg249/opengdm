package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderFilePreviewDTO extends DTOBase {

    private Integer id;
    private String directoryName;
    private String previewFileName;
    private List<File> fileList =  new ArrayList<File>();
    private List<String[]> filePreview = new ArrayList<String[]>(); //will contain the A list of 50 rows with 50 items each.


    @Override
    public Integer getId() {
        return this.id;
    }


    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getPreviewFileName() {
        return previewFileName;
    }

    public void setPreviewFileName(String previewFileName) {
        this.previewFileName = previewFileName;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public List<String[]> getFilePreview() {
        return filePreview;
    }

    public void setFilePreview(List<String[]> filePreview) {
        this.filePreview = filePreview;
    }

}
