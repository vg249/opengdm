package org.gobiiproject.gobiisampletrackingdao;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class DaoTestSetUp {

    private EntityManager em;
    private CvDao cvDao;

    public DaoTestSetUp(EntityManager em, CvDao cvDao) {
        this.em = em;
        this.cvDao = cvDao;

    }

    final int testPageSize = 10;

    Random random = new Random();

    private List<DnaRun> createdDnaRuns = new ArrayList<>();
    private List<Project> createdProjects = new ArrayList<>();
    private List<Germplasm> createdGermplasms = new ArrayList<>();
    private List<Experiment> createdExperiments = new ArrayList<>();
    private List<Contact> createdContacts = new ArrayList<>();
    private List<DnaSample> createdDnaSamples = new ArrayList<>();
    private List<Analysis> createdAnalyses = new ArrayList<>();
    private List<Dataset> createdDatasets = new ArrayList<>();

    public void createTestContacts(int numOfContacts) {
        for(int i = 0; i < numOfContacts; i++) {
            //Add PI contact
            Contact contact = new Contact();
            contact.setFirstName(RandomStringUtils.random(7, true, true));
            contact.setLastName(RandomStringUtils.random(7, true, true));
            contact.setCode(RandomStringUtils.random(7, true, true));
            contact.setEmail(RandomStringUtils.random(7, true, true));
            em.persist(contact);
            createdContacts.add(contact);
        }
    }

    public void createTestProjects(int numOfProjects) {
        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        if(createdContacts.size() == 0) {
            createTestContacts(Math.round(numOfProjects/2));
        }
        for(int i = 0; i < numOfProjects; i++) {
            //Add Project
            Project project = new Project();;
            project.setContact(createdContacts
                .get(random.nextInt(createdContacts.size())));
            project.setProjectName(RandomStringUtils.random(7, true, true));
            project.setStatus(newStatus);
            em.persist(project);
            createdProjects.add(project);
        }
    }

    public void createTestGermplasms(int numOfGermplasms) {
        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        for(int i = 0; i < numOfGermplasms; i++) {
            Germplasm germplasm = new Germplasm();
            germplasm.setGermplasmName(
                RandomStringUtils.random(7, true, true));
            germplasm.setStatus(newStatus);
            em.persist(germplasm);
            createdGermplasms.add(germplasm);
        }
    }

    public void createTestDnaSamples(int numOfDnaSamples) {
        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        if(createdGermplasms.size() == 0) {
            createTestGermplasms(Math.round(numOfDnaSamples/2));
        }
        if(createdProjects.size() == 0) {
            createTestProjects(Math.round(numOfDnaSamples/2));
        }
        for(int i = 0; i < numOfDnaSamples; i++) {
            DnaSample dnaSample = new DnaSample();
            dnaSample.setDnaSampleName(
                RandomStringUtils.random(7, true, true));
            dnaSample.setProjectId(
                createdProjects
                    .get(random.nextInt(createdProjects.size()))
                    .getProjectId());
            dnaSample.setGermplasm(
                createdGermplasms
                    .get(random.nextInt(createdGermplasms.size())));
            dnaSample.setStatus(newStatus);
            dnaSample.setDnaSampleUuid(
                RandomStringUtils.random(10, true, true));
            em.persist(dnaSample);
            createdDnaSamples.add(dnaSample);
        }
    }

    public void createTestExperiments(int numOfExperiments) {

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        if(createdProjects.size() == 0) {
            createTestProjects(Math.round(numOfExperiments/2));
        }

        //Add Experiment
        for(int i = 0; i < numOfExperiments; i++){

            Experiment experiment = new Experiment();

            experiment.setExperimentName(
                RandomStringUtils.random(7, true, true));
            experiment.setExperimentCode(
                RandomStringUtils.random(7, true, true));
            experiment.setProject(createdProjects.get(
                random.nextInt(createdProjects.size())));
            experiment.setStatus(newStatus);

            VendorProtocol vendorProtocol = new VendorProtocol();

            Organization vendor = new Organization();
            vendor.setName(RandomStringUtils.random(7, true, true));
            vendor.setStatus(newStatus);

            Protocol protocol = new Protocol();
            protocol.setName(RandomStringUtils.random(7, true, true));

            em.persist(vendor);
            em.persist(protocol);

            vendorProtocol.setProtocol(protocol);
            vendorProtocol.setVendor(vendor);
            em.persist(vendorProtocol);

            experiment.setVendorProtocol(vendorProtocol);
            em.persist(experiment);

            createdExperiments.add(experiment);
        }
    }

    public void createTestAnalyses(int numOfAnalysis) {
        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        List<Cv> analysisTypes = cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        for(int i = 0; i < numOfAnalysis; i++) {
            Analysis analysis = new Analysis();
            assertTrue("System defined analysis type values are not found.",
                analysisTypes.size() > -1);
            Integer analysisTypeIndex = random.nextInt(analysisTypes.size());
            String analysisName = RandomStringUtils.random(7, true, true);
            analysis.setAnalysisName(analysisName);
            analysis.setType(analysisTypes.get(analysisTypeIndex));
            analysis.setStatus(newStatus);
            em.persist(analysis);
            createdAnalyses.add(analysis);
        }
    }

    public void createTestDatasets(int numOfDatasets) {

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        List<Cv> datasetTypes = cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_DATASET_TYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        if(createdExperiments.size() == 0) {
            createTestExperiments(numOfDatasets/2);
        }
        if(createdAnalyses.size() == 0) {
            createTestAnalyses(numOfDatasets/2);
        }

        for(int i = 0; i < numOfDatasets; i++) {
            Dataset dataset = new Dataset();

            String datasetName = RandomStringUtils.random(7, true, true);
            dataset.setDatasetName(datasetName);
            dataset.setCallingAnalysis(createdAnalyses
                .get(random.nextInt(createdAnalyses.size())));
            dataset.setExperiment(createdExperiments
                .get(random.nextInt(createdExperiments.size())));
            dataset.setType(datasetTypes
                .get(random.nextInt(datasetTypes.size())));

            em.persist(dataset);
            createdDatasets.add(dataset);

        }

    }

    public void createTestDnaRuns(int numOfDnaRuns) {

        if(createdDnaSamples.size() == 0) {
            createTestDnaSamples(Math.round(numOfDnaRuns/2));
        }
        if(createdExperiments.size() == 0) {
            createTestExperiments(Math.round(numOfDnaRuns/2));
        }
        for(int i = 0; i < testPageSize; i++) {
            DnaRun dnaRun = new DnaRun();

            dnaRun.setDnaRunName(RandomStringUtils.random(7, true, true));
            dnaRun.setExperiment(
                createdExperiments
                    .get(random.nextInt(createdExperiments.size())));
            dnaRun.setDnaSample(
                createdDnaSamples.get(random.nextInt(createdDnaSamples.size())));
            em.persist(dnaRun);

            createdDnaRuns.add(dnaRun);
        }

    }

    public List<DnaRun> getCreatedDnaRuns() {
        return createdDnaRuns;
    }

    public void setCreatedDnaRuns(List<DnaRun> createdDnaRuns) {
        this.createdDnaRuns = createdDnaRuns;
    }

    public List<Germplasm> getCreatedGermplasms() {
        return createdGermplasms;
    }

    public void setCreatedGermplasms(List<Germplasm> createdGermplasms) {
        this.createdGermplasms = createdGermplasms;
    }

    public List<Experiment> getCreatedExperiments() {
        return createdExperiments;
    }

    public void setCreatedExperiments(List<Experiment> createdExperiments) {
        this.createdExperiments = createdExperiments;
    }

    public List<Contact> getCreatedContacts() {
        return createdContacts;
    }

    public void setCreatedContacts(List<Contact> createdContacts) {
        this.createdContacts = createdContacts;
    }

    public List<Project> getCreatedProjects() {
        return createdProjects;
    }

    public void setCreatedProjects(List<Project> createdProjects) {
        this.createdProjects = createdProjects;
    }

    public List<DnaSample> getCreatedDnaSamples() {
        return createdDnaSamples;
    }

    public void setCreatedDnaSamples(List<DnaSample> createdDnaSamples) {
        this.createdDnaSamples = createdDnaSamples;
    }

    public List<Analysis> getCreatedAnalyses() {
        return createdAnalyses;
    }

    public void setCreatedAnalyses(List<Analysis> createdAnalyses) {
        this.createdAnalyses = createdAnalyses;
    }

    public List<Dataset> getCreatedDatasets() {
        return createdDatasets;
    }

    public void setCreatedDatasets(List<Dataset> createdDatasets) {
        this.createdDatasets = createdDatasets;
    }
}
