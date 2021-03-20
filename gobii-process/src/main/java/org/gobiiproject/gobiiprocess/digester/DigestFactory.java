package org.gobiiproject.gobiiprocess.digester;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstructionV3;
import org.gobiiproject.gobiiprocess.digester.digest3.Digest3;
import org.gobiiproject.gobiiprocess.digester.digest3.HapmapsDigest;
import org.gobiiproject.gobiiprocess.digester.digest3.InterteksDigest;
import org.gobiiproject.gobiiprocess.digester.digest3.MarkersDigest;
import org.gobiiproject.gobiiprocess.digester.digest3.SamplesDigest;
import org.gobiiproject.gobiiprocess.digester.digest3.VcfDigest;

public class DigestFactory {

    public Digest getDigest(String loaderInstructionContents,
                             ConfigSettings configSettings) throws GobiiException {

        ObjectMapper mapper = new ObjectMapper();
        
        LoaderInstructionV3 loaderInstruction;

        try {
            loaderInstruction = mapper.readValue(
                loaderInstructionContents,
                LoaderInstructionV3.class);
        }
        catch(IOException e) {
            throw new GobiiException(e);
        }
        
        switch (loaderInstruction.getLoadType()) {
            case "SAMPLES":
                return new SamplesDigest(loaderInstruction, configSettings);
            case "MARKER":
                return new MarkersDigest(loaderInstruction, configSettings);
            case "MATRIX":
                GenotypeUploadRequestDTO uploadRequest = mapper.convertValue(
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
