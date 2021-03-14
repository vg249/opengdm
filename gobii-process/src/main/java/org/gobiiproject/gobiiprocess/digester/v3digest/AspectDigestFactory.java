package org.gobiiproject.gobiiprocess.digester.v3digest;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;

public class AspectDigestFactory {

    public AspectDigest getDigest(LoaderInstruction loaderInstruction,
                                  ConfigSettings configSettings) {
        switch (loaderInstruction.getLoadType()) {
            case "SAMPLES":
                return new SamplesDigest(loaderInstruction, configSettings);
            case "MARKER":
                return new MarkersDigest(loaderInstruction, configSettings);
            case "MATRIX":
                GenotypeUploadRequestDTO uploadRequest = AspectDigest.mapper.convertValue(
                    loaderInstruction.getUserRequest(), 
                    GenotypeUploadRequestDTO.class);                    
                switch(uploadRequest.getFileType()) {
                    case HAPMAP:
                        return new HapmapsDigest(loaderInstruction, configSettings);
                    case VCF:
                        return new VcfDigest(loaderInstruction, configSettings);
                    case INTERTEK:
                        return new InterteksDigest(loaderInstruction, configSettings);
                    default:
                        throw new GobiiException("Invalid loader instruction");
                }
            default:
                throw new GobiiException("Invalid loader instruction");
        }
    }
}
