package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public interface LoaderFileDAO {

    List<LoaderInstruction> getSampleInstructions();
    String writeInstructions(String fileUniqueId, List<LoaderInstruction> instructions)  throws GobiiDaoException;
}
