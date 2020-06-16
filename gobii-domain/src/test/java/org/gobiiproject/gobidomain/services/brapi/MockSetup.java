package org.gobiiproject.gobidomain.services.brapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.util.*;

public class MockSetup {

    Random random = new Random();

    public List<Germplasm> mockGermplasms;
    public List<DnaSample> mockDnaSamples;
    public List<Experiment> mockExperiments;
    public List<DnaRun> mockDnaRuns;
    public List<Cv> mockGermplasmProps;
    public List<Cv> mockDnaSampleProps;
    public List<Cv> moockDnaRunsProps;
    public List<Mapset> mockMapSets;
    public List<Cv> mockMapSetTypes;
    public List<Platform> mockPlatforms;
    public List<Marker> mockMarkers;
    public List<Dataset> mockDatasets;


    String[] testDatasetIds = {"1", "2", "3"};

    String[] genotypes = {"G", "C", "A", "T"};

    public CvGroup createMockCvGroup(GobiiCvGroupType gobiiCvGroupType, CvGroupTerm cvGroupTerm) {

        CvGroup cvGroup = new CvGroup();
        cvGroup.setCvGroupId(random.nextInt(100));
        cvGroup
            .setCvGroupName(cvGroupTerm.getCvGroupName());
        cvGroup
            .setCvGroupType(
                gobiiCvGroupType.getGroupTypeId());

        return cvGroup;
    }

    public List<Cv> createMockProps(int numProps, CvGroupTerm cvGroupTerm) {

        List<Cv> props = new ArrayList<>();

        List<CvGroup> cvGroups = new ArrayList<>();

        cvGroups.add(createMockCvGroup(GobiiCvGroupType.GROUP_TYPE_SYSTEM,
            CvGroupTerm.CVGROUP_GERMPLASM_PROP));

        cvGroups.add(createMockCvGroup(GobiiCvGroupType.GROUP_TYPE_USER,
            CvGroupTerm.CVGROUP_GERMPLASM_PROP));

        for(int i = 0; i < numProps; i ++) {
            Cv cv = new Cv();
            cv.setCvId(random.nextInt(1000));
            cv.setCvGroup(cvGroups.get(random.nextInt(cvGroups.size())));
            cv.setTerm(RandomStringUtils.random(4,true,true));
            props.add(cv);
        }

        return props;

    }

    public void createMockGermplasmProps(int numProps) {
        mockGermplasmProps = createMockProps(numProps, CvGroupTerm.CVGROUP_GERMPLASM_PROP);
    }

    public void createMockMapSetTypes(int numTypes) {
        mockMapSetTypes = createMockProps(numTypes, CvGroupTerm.CVGROUP_MAPSET_TYPE);
    }

    public void createMockDnaSampleProps(int numProps) {
        mockDnaSampleProps = createMockProps(numProps, CvGroupTerm.CVGROUP_DNASAMPLE_PROP);
    }

    public void createMockExperiments(int numExperiments) {
        mockExperiments = new ArrayList<>();
        for(int i = 0; i < numExperiments; i++) {
            Experiment experiment = new Experiment();
            experiment.setExperimentId(i+1);
            experiment.setExperimentName(RandomStringUtils.random(7, true, true));
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
            germplasm.setGermplasmId(i+1);
            germplasm.setGermplasmName(RandomStringUtils.random(7, true, true));
            germplasm.setExternalCode(UUID.randomUUID().toString());
            ObjectNode properties = JsonNodeFactory.instance.objectNode();
            properties.put(
                mockGermplasmProps
                    .get(random.nextInt(mockGermplasmProps.size()))
                    .getCvId().toString(),
                RandomStringUtils.random(5, true, true));
            properties.put(
                mockGermplasmProps
                    .get(random.nextInt(mockGermplasmProps.size()))
                    .getCvId().toString(),
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

        if(CollectionUtils.isEmpty((mockDnaSampleProps))) {
            createMockDnaSampleProps(5);
        }

        for(int i = 0; i < numDnaSamples; i++) {

            DnaSample dnaSample = new DnaSample();

            dnaSample.setDnaSampleId(i+1);
            dnaSample.setDnaSampleName(RandomStringUtils.random(7, true, true));
            dnaSample.setDnaSampleNum(RandomStringUtils.random(4, false, true));
            dnaSample.setDnaSampleUuid(UUID.randomUUID().toString());
            dnaSample.setProjectId(i);

            ObjectNode properties = JsonNodeFactory.instance.objectNode();

            properties.put(
                mockDnaSampleProps
                    .get(random.nextInt(mockDnaSampleProps.size()))
                    .getCvId().toString(),
                RandomStringUtils.random(5, true, true));
            properties.put(
                mockDnaSampleProps
                    .get(random.nextInt(mockDnaSampleProps.size()))
                    .getCvId().toString(),
                RandomStringUtils.random(5, true, true));

            dnaSample.setProperties(properties);
            dnaSample.setGermplasm(mockGermplasms.get(random.nextInt(mockGermplasms.size())));

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

        Map<String, Integer> maxHdf5Index = new HashMap<>();

        for(Integer i = 0; i < numDnaRuns; i++) {

            DnaRun dnaRun = new DnaRun();

            dnaRun.setDnaRunId(i+1);
            dnaRun.setDnaRunName(RandomStringUtils.random(7, true, true));

            ObjectNode datasetDnarunIndex = JsonNodeFactory.instance.objectNode();
            String testDatasetId = testDatasetIds[random.nextInt(testDatasetIds.length)];
            Integer hdf5Index = maxHdf5Index.getOrDefault(testDatasetId, 0);
            datasetDnarunIndex.put(testDatasetId, hdf5Index.toString());
            hdf5Index++;
            maxHdf5Index.put(testDatasetId, hdf5Index);
            dnaRun.setDatasetDnaRunIdx(datasetDnarunIndex);

            dnaRun.setDnaSample(mockDnaSamples.get(random.nextInt(mockDnaSamples.size())));
            dnaRun.setExperiment(mockExperiments.get(random.nextInt(mockExperiments.size())));

            mockDnaRuns.add(dnaRun);

        }
    }

    public void createMockMapSets(int numMapSets) {

        mockMapSets = new ArrayList<>();

        if(CollectionUtils.isEmpty(mockMapSetTypes)) {
            createMockMapSetTypes(3);
        }

        for(Integer i = 0; i < numMapSets; i++) {

            Mapset mapset = new Mapset();
            mapset.setMapsetId(i+1);
            mapset.setMapsetName(RandomStringUtils.random(7, true, true));
            mapset.setMapsetCode(RandomStringUtils.random(7, true, true));
            mapset.setMapsetDescription(RandomStringUtils.random(7, true, true));
            mapset.setMarkerCount(random.nextInt(901)+100);
            mapset.setLinkageGroupCount(random.nextInt(12));

            mapset.setType(
                mockMapSetTypes.get(
                    random.nextInt(mockMapSetTypes.size())));
            mockMapSets.add(mapset);

        }
    }

    public void createMockPlatform(int numPlatforms) {
        mockPlatforms = new ArrayList<>();

        for(Integer i = 0; i < numPlatforms; i++) {

            Platform platform = new Platform();
            platform.setPlatformId(i+1);
            platform.setPlatformName(RandomStringUtils.random(7, true, true));
            platform.setDescription(RandomStringUtils.random(7, true, true));

            mockPlatforms.add(platform);
        }

    }

    public void createMockMarkers(int numMarkers) {

        mockMarkers = new ArrayList<>();

        if(CollectionUtils.isEmpty(mockPlatforms)) {
            createMockPlatform(Math.round(numMarkers/2));
        }

        Map<String, Integer> maxHdf5Index = new HashMap<>();

        for(Integer i = 0; i < numMarkers; i++) {
            Marker marker = new Marker();

            marker.setMarkerId(i+1);
            marker.setMarkerName(RandomStringUtils.random(7, true, true));
            marker.setPlatform(mockPlatforms.get(random.nextInt(mockPlatforms.size())));
            marker.setRef(genotypes[random.nextInt(genotypes.length)]);

            //Set alts
            int altsSize = random.nextInt(4);
            String[] alts = new String[altsSize];

            for(int j = 0; i < altsSize; i++) {
                alts[j] =  genotypes[random.nextInt(genotypes.length)];
            }
            marker.setAlts(alts);

            ObjectNode datasetMarkerIndex = JsonNodeFactory.instance.objectNode();
            String testDatasetId = testDatasetIds[random.nextInt(testDatasetIds.length)];
            Integer hdf5Index = maxHdf5Index.getOrDefault(testDatasetId, 0);
            datasetMarkerIndex.put(testDatasetId, hdf5Index.toString());
            hdf5Index++;
            maxHdf5Index.put(testDatasetId, hdf5Index);
            marker.setDatasetMarkerIdx(datasetMarkerIndex);
        }

    }


    public void createMockDatasets() {


    }

}