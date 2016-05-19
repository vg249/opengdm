package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public interface LoaderInstructionsDAO {

    List<GobiiLoaderInstruction> getSampleInstructions();
    String writeInstructions(String loaderDestinationPath, List<GobiiLoaderInstruction> instructions)  throws GobiiDaoException;
}
