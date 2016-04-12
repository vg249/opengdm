package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DtoMetaData {

    public List<LoaderInstruction> getLoaderInstructions() {
        return loaderInstructions;
    }

    public void setLoaderInstructions(List<LoaderInstruction> loaderInstructions) {
        this.loaderInstructions = loaderInstructions;
    }

    private List<LoaderInstruction> loaderInstructions  = new ArrayList<>();
}
