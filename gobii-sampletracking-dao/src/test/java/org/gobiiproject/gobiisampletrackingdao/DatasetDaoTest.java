package org.gobiiproject.gobiisampletrackingdao;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class DatasetDaoTest {


    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private CvDao cvDao;

    Random random = new Random();

    final Integer testPageSize = 10;

    List<Integer> createdDatasetIds = new ArrayList<>();
    List<Integer> createdExperimentIds = new ArrayList<>();

    @Before
    public void createTestData() {

        if(createdDatasetIds.size() > 0) {
            return;
        }

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        //Add Contact
        Contact piContact = new Contact();
        piContact.setFirstName(RandomStringUtils.random(7, true, true));
        piContact.setLastName(RandomStringUtils.random(7, true, true));
        piContact.setCode(RandomStringUtils.random(7, true, true));
        piContact.setEmail(RandomStringUtils.random(7, true, true));
        em.persist(piContact);

        //Add Project
        Project project = new Project();
        project.setContact(piContact);
        project.setProjectName(RandomStringUtils.random(7, true, true));
        project.setStatus(newStatus);
        em.persist(project);

        //Add Experiments
        List<Experiment> experiments = new ArrayList<>();

        for(int i = 0; i < (testPageSize/5); i++) {

            Experiment experiment = new Experiment();
            experiment.setExperimentName(
                RandomStringUtils.random(7, true, true));
            experiment.setExperimentCode(
                RandomStringUtils.random(7, true, true));
            experiment.setProject(project);

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

            experiments.add(experiment);

        }

        //Add Calling Analysis
        Analysis callingAnalysis = new Analysis();
        List<Cv> analysisTypes = cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        assertTrue("System defined analysis type values are not found.",
            analysisTypes.size() > -1);
        Integer analysisTypeIndex = random.nextInt(analysisTypes.size());
        String analysisName = RandomStringUtils.random(7, true, true);
        callingAnalysis.setAnalysisName(analysisName);
        callingAnalysis.setType(analysisTypes.get(analysisTypeIndex));
        callingAnalysis.setStatus(newStatus);
        em.persist(callingAnalysis);

        for (int i = 0; i < testPageSize; i++) {

            String datasetName = RandomStringUtils.random(7, true, true);



            Dataset dataset = new Dataset();

            Experiment experiment = experiments.get(
                random.nextInt(experiments.size()));

            dataset.setDatasetName(datasetName);
            dataset.setCallingAnalysis(callingAnalysis);
            dataset.setExperiment(experiment);

            em.persist(dataset);

            createdDatasetIds.add(dataset.getDatasetId());
            createdExperimentIds.add(experiment.getExperimentId());
        }
        em.flush();

    }

    @Test
    public void testListDatasets() {

        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(
                testPageSize, testRowOffset,
                null, null,
                null, null);


        assertTrue(datasets.size() <= testPageSize);

    }


    @Test
    public void testListDatasetsWithPageSize() {

        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(
            testPageSize, testRowOffset,
            null, null,
            null, null);

        assertTrue("Dataset Page Size condition failed",
            datasets.size() <= testPageSize && datasets.size() > 0);

    }

    @Test
    public void testGetDatasetById() {

        Integer datasetId = createdDatasetIds
            .get(random.nextInt(createdDatasetIds.size()));

        Dataset dataset = datasetDao.getDatasetById(datasetId);

        assertTrue("Failing get Dataset by Id",
                dataset.getDatasetId() == datasetId);

    }

    @Test
    public void testGetDatasetByExperiemntId() {

        Integer experimentId =
            createdExperimentIds
                .get(random.nextInt(createdExperimentIds.size()));

        List<Dataset> datasetsByExperimentId =
            datasetDao.getDatasets(testPageSize, 0,
                null, null,
                experimentId, null);

        assertTrue("No Dataset to test",
            datasetsByExperimentId.size() <= testPageSize
            && datasetsByExperimentId.size() > 0);

        for (Dataset dataset : datasetsByExperimentId) {
            assertTrue("Failing Experiment Id Filter",
                    dataset.getExperiment().getExperimentId() == experimentId);
        }

    }

    @Test
    public void testGetDatasetsWithAnalysisAndCounts() {


        List<Object[]> resultTuple =
            datasetDao.getDatasetsWithAnalysesAndCounts(
                testPageSize, 0,
                null, null,
                null, null);


        assertTrue("Failed getDatasetsWithAnalysisAndCounts",
            resultTuple.size() <= testPageSize && resultTuple.size() > 0);

    }
}
