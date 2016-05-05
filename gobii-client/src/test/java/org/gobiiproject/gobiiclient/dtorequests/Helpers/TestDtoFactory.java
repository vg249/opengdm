package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.*;

import java.util.Date;

/**
 * Created by Phil on 4/27/2016.
 */
public class TestDtoFactory {

    public static EntityParamValues makeArbitraryEntityParams() {

        EntityParamValues returnVal = new EntityParamValues();


        returnVal.add("fooparam","fooval");
        returnVal.add("barparam","barval");
        returnVal.add("foobarparam","foobarval");

        return returnVal;
    }

    public static AnalysisDTO makePopulatedAnalysisDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        AnalysisDTO returnVal = new AnalysisDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        returnVal.setAnalysisName(uniqueStem + ": analysis");
        returnVal.setTimeExecuted(new Date());
        returnVal.setSourceUri(uniqueStem + ":  foo URL");
        returnVal.setAlgorithm(uniqueStem + ":  foo algorithm");
        returnVal.setSourceName(uniqueStem + ":  foo source");
        returnVal.setAnalysisDescription(uniqueStem + ":  my analysis description");
        returnVal.setProgram(uniqueStem + ":  foo program");
        returnVal.setProgramVersion(uniqueStem + ":  foo version");
        returnVal.setAnlaysisTypeId(1);
        returnVal.setStatus(1);

        returnVal.setParameters(entityParamValues.getProperties());

        return returnVal;

    }


    public static MapsetDTO makePopulatedMapsetDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem,
                                                     EntityParamValues entityParamValues) {

        MapsetDTO returnVal = new MapsetDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        // set the plain properties
        returnVal.setName(uniqueStem + "dummy name");
        returnVal.setCode(uniqueStem + "add dummy code");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDescription(uniqueStem + "dummy description");
        returnVal.setMapType(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setReferenceId(1);
        returnVal.setStatus(1);

        returnVal.setParameters(entityParamValues.getProperties());

        return returnVal;

    }

    public static ReferenceDTO makePopulatedReferenceDTO(DtoMetaData.ProcessType processType,
                                                         Integer uniqueStem) {

        ReferenceDTO returnVal = new ReferenceDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        returnVal.setName(uniqueStem + ": reference");
        returnVal.setVersion("version:"+uniqueStem);
        returnVal.setLink(uniqueStem+" link");
        returnVal.setFilePath(uniqueStem +" file path");

        return returnVal;

    }


    public static ContactDTO makePopulatedContactDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem) {

        String uniqueStemString = uniqueStem.toString();
        ContactDTO returnVal = new ContactDTO(processType);
        // set the plain properties

        returnVal.setFirstName(uniqueStem +" new contact");
        returnVal.setLastName(uniqueStem +"new lastname");
        returnVal.setEmail(uniqueStem +"mail@email.com");
        returnVal.setCode(uniqueStem +"added New Code");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());

        returnVal.getRoles().add(1);
        returnVal.getRoles().add(2);

        return returnVal;

    }
}
