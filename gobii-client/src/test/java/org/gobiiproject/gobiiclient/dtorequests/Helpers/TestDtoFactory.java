package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;

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

    public static PlatformDTO makePopulatedPlatformDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem) {

        PlatformDTO returnVal = new PlatformDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        // set the plain properties
        returnVal.setStatus(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setPlatformCode(uniqueStem +"dummy code");
        returnVal.setPlatformDescription(uniqueStem +"dummy description");
        returnVal.setPlatformName(uniqueStem +"New Platform");
        returnVal.setPlatformVendor(1);
        returnVal.setTypeId(1);

        return returnVal;

    }

    public static DataSetDTO makePopulatedDataSetDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        DataSetDTO returnVal = new DataSetDTO(processType);


        // set the big-ticket items
        Integer analysisUniqueStem = 0;
        returnVal.setCallingAnalysis(TestDtoFactory.makePopulatedAnalysisDTO(
                processType,
                ++analysisUniqueStem,
                entityParamValues));

        returnVal.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                processType,
                ++analysisUniqueStem,
                entityParamValues));
        returnVal.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                processType,
                ++analysisUniqueStem,
                entityParamValues));
        returnVal.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                processType,
                ++analysisUniqueStem,
                entityParamValues));

        returnVal.getScores().add(1);
        returnVal.getScores().add(2);
        returnVal.getScores().add(3);

        // set the plain properties
        returnVal.setStatus(1);
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDataFile(uniqueStem + ": foo file");
        returnVal.setQualityFile(uniqueStem + ": foo quality file");
        returnVal.setExperimentId(2);
        returnVal.setDataTable(uniqueStem + ": foo table");
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());

        return returnVal;

    }
}
