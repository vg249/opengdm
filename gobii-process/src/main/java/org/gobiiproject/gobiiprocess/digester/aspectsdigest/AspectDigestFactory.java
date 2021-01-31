package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;

public class AspectDigestFactory {

    public AspectDigest getDigest(LoaderInstruction loaderInstruction,
                                  ConfigSettings configSettings) {
        switch (loaderInstruction.getLoadType()) {
            case "SAMPLES":
                return new SamplesDigest(loaderInstruction, configSettings);
            default:
                throw new GobiiException("Invalid loader instruction");
        }
    }
}
