package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;

public class MarkersDigest extends AspectDigest {

    MarkersDigest(LoaderInstruction loaderInstruction,
                  ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction, configSettings);
    }

    public DigesterResult digest() throws GobiiException {
        return null;
    }


}
