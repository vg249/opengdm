package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DtoMetaData {

    public LoaderInstructionFilesDTO() {
        super(ProcessType.CREATE);
    }

    private List<GobiiLoaderInstruction> gobiiLoaderInstructions = new ArrayList<>();
    private String instructionFileName = null;
    private String rawUserFilePath = null;
    private String intermediateFilesDirectory = null;
    private boolean isCreateSourceFile = true;
    boolean requireDirectoriesToExist = false;

    public List<GobiiLoaderInstruction> getGobiiLoaderInstructions() {
        return gobiiLoaderInstructions;
    }

    public void setGobiiLoaderInstructions(List<GobiiLoaderInstruction> gobiiLoaderInstructions) {
        this.gobiiLoaderInstructions = gobiiLoaderInstructions;
    }

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }

    public String getRawUserFilePath() {
        return rawUserFilePath;
    }

    public void setRawUserFilePath(String rawUserFilePath) {
        this.rawUserFilePath = rawUserFilePath;
    }

    public String getIntermediateFilesDirectory() {
        return intermediateFilesDirectory;
    }

    public void setIntermediateFilesDirectory(String intermediateFilesDirectory) {
        this.intermediateFilesDirectory = intermediateFilesDirectory;
    }

    public boolean isCreateSourceFile() {
        return isCreateSourceFile;
    }

    public void setCreateSourceFile(boolean createSourceFile) {
        this.isCreateSourceFile = createSourceFile;
    }

    public boolean isRequireDirectoriesToExist() {
        return requireDirectoriesToExist;
    }

    public void setRequireDirectoriesToExist(boolean requireDirectoriesToExist) {
        this.requireDirectoriesToExist = requireDirectoriesToExist;
    }
}
