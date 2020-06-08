package org.gobiiproject.gobidomain.services.brapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.entity.pgsql.CvPropertiesType;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MockSetup {

    Random random = new Random();

    public List<Germplasm> mockGermplasms;
    public List<DnaSample> mockDnaSamples;
    public List<Experiment> mockExperiments;
    public List<DnaRun> mockDnaRuns;
    public List<Cv> mockGermplasmProps;
    public List<Cv> mockDnaSampleProps;
    public List<Cv> moockDnaRunsProps;

    String[] testDatasetIds = {"1", "2", "3"};

    public void createMockGermplasmProps(int numProps) {

        mockGermplasmProps = new ArrayList<>();

        CvGroup germplasPropGroup = new CvGroup();

        germplasPropGroup.setCvGroupId(random.nextInt());
        germplasPropGroup
            .setCvGroupName(RandomStringUtils.random(5, true,true));
        germplasPropGroup.setCvGroupType(
            GobiiCvGroupType.GROUP_TYPE_SYSTEM.getGroupTypeId());

        for(int i = 0; i < numProps; i ++) {
            Cv germplasmProp = new Cv();
            germplasmProp.setCvId(random.nextInt());
            germplasmProp.setCvGroup(germplasPropGroup);
            germplasmProp.setTerm(
                RandomStringUtils.random(4,true,true));
            mockGermplasmProps.add(germplasmProp);
        }
    }

    public void createMockExperiments(int numExperiments) {
        mockExperiments = new ArrayList<>();
        for(int i = 0; i < numExperiments; i++) {
            Experiment experiment = new Experiment();
            experiment.setExperimentId(random.nextInt());
            experiment.setExperimentName(
                RandomStringUtils.random(7, true, true));
            mockExperiments.add(experiment);
        }
    }

    public void createMockGermplsms(int numGermplasms) {
        mockGermplasms = new ArrayList<>();
        if(CollectionUtils.isEmpty(mockGermplasmProps)) {
            createMockGermplasmProps(5);
        }
        for(int i = 0; i < numGermplasms; i++) {
            Germplasm germplasm = new Germplasm();
            germplasm.setGermplasmId(i);
            germplasm.setGermplasmName(RandomStringUtils.random(7, true, true));
            germplasm.setExternalCode(UUID.randomUUID().toString());
            ObjectNode properties = JsonNodeFactory.instance.objectNode();
            properties.put(
                mockGermplasmProps.get(
                    random.nextInt(mockGermplasmProps.size())
                ).getCvId().toString(),
                RandomStringUtils.random(5, true, true));
            properties.put(
                mockGermplasmProps.get(
                    random.nextInt(mockGermplasmProps.size())
                ).getCvId().toString(),
                RandomStringUtils.random(5, true, true));
            germplasm.setProperties(properties);
            mockGermplasms.add(germplasm);
        }
    }

    public void createMockDnaSamples(int numDnaSamples) {

        mockDnaSamples = new ArrayList<>();

        if(CollectionUtils.isEmpty(mockGermplasms)) {
            createMockGermplsms(Math.round(numDnaSamples / 2));
        }

        for(int i = 0; i < numDnaSamples; i++) {
            DnaSample dnaSample = new DnaSample();
            ObjectNode properties = JsonNodeFactory.instance.objectNode();
            dnaSample.setDnaSampleId(i+1);
            dnaSample.setDnaSampleName(RandomStringUtils.random(7, true, true));
            dnaSample.setDnaSampleNum(RandomStringUtils.random(4, false, true));
            dnaSample.setDnaSampleUuid(UUID.randomUUID().toString());
            dnaSample.setProjectId(i);
            dnaSample.setProperties(properties);
            dnaSample.setGermplasm(mockGermplasms.get(
                random.nextInt(mockGermplasms.size())));
            mockDnaSamples.add(dnaSample);
        }

    }

    public void createMockDnaRuns(int numDnaRuns) {

        mockDnaRuns = new ArrayList<>();

        if(CollectionUtils.isEmpty(mockDnaSamples)) {
            createMockDnaSamples(Math.round(numDnaRuns / 2));
        }
        if(CollectionUtils.isEmpty(mockExperiments)) {
            createMockExperiments(Math.round(numDnaRuns / 2));
        }

        for(Integer i = 0; i < numDnaRuns; i++) {
            DnaRun dnaRun = new DnaRun();

            dnaRun.setDnaRunName(RandomStringUtils.random(7, true, true));

            ObjectNode datasetDnarunIndex =
                JsonNodeFactory.instance.objectNode();

            datasetDnarunIndex.put(
                testDatasetIds[random.nextInt(testDatasetIds.length)],
                i.toString());
            dnaRun.setDatasetDnaRunIdx(datasetDnarunIndex);

            dnaRun.setDnaSample(mockDnaSamples
                .get(random.nextInt(mockDnaSamples.size())));

            dnaRun.setExperiment(mockExperiments
                .get(random.nextInt(mockExperiments.size())));

            mockDnaRuns.add(dnaRun);

        }

    }
}
