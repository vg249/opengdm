package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

import java.util.Date;

/**
 * Created by Phil on 4/27/2016.
 */
public class TestDtoFactory {
    
    public static AnalysisDTO makePopulatedAnalysisDTO(DtoMetaData.ProcessType processType, Integer uniqueStem) {

        AnalysisDTO returnVal = new AnalysisDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        returnVal.setAnalysisName(uniqueStem +": analysis");
        returnVal.setTimeExecuted(new Date());
        returnVal.setSourceUri(uniqueStem +":  foo URL");
        returnVal.setAlgorithm(uniqueStem +":  foo algorithm");
        returnVal.setSourceName(uniqueStem +":  foo source");
        returnVal.setAnalysisDescription(uniqueStem +":  my analysis description");
        returnVal.setProgram(uniqueStem +":  foo program");
        returnVal.setProgramVersion(uniqueStem +":  foo version");
        returnVal.setAnlaysisTypeId(1);
        returnVal.setStatus(1);

        returnVal.getParameters().add(new EntityPropertyDTO(null,null,"foo param","foo val"));
        returnVal.getParameters().add(new EntityPropertyDTO(null,null,"bar param","bar val"));
        returnVal.getParameters().add(new EntityPropertyDTO(null,null,"bar param","bar val"));

        return  returnVal;
       
    }
}
