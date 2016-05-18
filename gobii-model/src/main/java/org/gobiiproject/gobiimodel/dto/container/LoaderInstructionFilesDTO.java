package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DtoMetaData {

    private List<GobiiLoaderInstruction> gobiiLoaderInstructions = new ArrayList<>();
    String userName = null;
    String outputFileId = null;

    public List<GobiiLoaderInstruction> getGobiiLoaderInstructions() {
        return gobiiLoaderInstructions;
    }

    public void setGobiiLoaderInstructions(List<GobiiLoaderInstruction> gobiiLoaderInstructions) {
        this.gobiiLoaderInstructions = gobiiLoaderInstructions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOutputFileId() {
        return outputFileId;
    }

    public void setOutputFileId(String outputFileId) {
        this.outputFileId = outputFileId;
    }
}
