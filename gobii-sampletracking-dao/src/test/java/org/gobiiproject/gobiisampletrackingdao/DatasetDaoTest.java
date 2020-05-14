package org.gobiiproject.gobiisampletrackingdao;

import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;

/**
 * This tests are created with knowledge of exisiting data
 * in api.gobii.org:gobii-dev database
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class DatasetDaoTest {

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private  CvDao cvDao;

    @Autowired
    private  ExperimentDao experimentDao;

    @Autowired
    private AnalysisDao analysisDao;

    Random random = new Random();

    static final Integer testPageSize = 10;

    static Set<Integer> createdDatasetIds = new HashSet<>();


    @Before
    public void createTestData() {

        Random random = new Random();

        List<Cv> analysisTypes = cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        assertTrue("System defined analysis type values are not found.",
            analysisTypes.size() > 0);

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        List<Experiment> experiments = experimentDao.getExperiments(
            100, 0, null);

        Integer analysisTypeIndex = random.nextInt(analysisTypes.size());
        String analysisName = RandomStringUtils.random(7, true, true);

        Analysis callingAnalysis = new Analysis();
        callingAnalysis.setAnalysisName(analysisName);
        callingAnalysis.setType(analysisTypes.get(analysisTypeIndex));
        callingAnalysis.setStatus(newStatus);

        analysisDao.createAnalysis(callingAnalysis);

        for (int i = 0; i < testPageSize; i++) {

            String datasetName = RandomStringUtils.random(7, true, true);

            Dataset dataset = new Dataset();

            dataset.setDatasetName(datasetName);
            dataset.setCallingAnalysis(callingAnalysis);
            dataset.setExperiment(experiments.get(0));

            try {
                datasetDao.createDataset(dataset);
            }
            catch (Exception e) {
                TestCase.fail("Unknown Exception: "+ e.getMessage());
            }

            createdDatasetIds.add(dataset.getDatasetId());
        }

    }

    @Test
    public void testListDatasets() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(
                testPageSize, testRowOffset,
                null, null,
                null, null);


        assertTrue(datasets.size() <= testPageSize);

    }


    @Test
    public void testListDatasetsWithPageSize() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null, null);

        assertTrue(datasets.size() == 10);

    }

    @Test
    public void testGetDatasetById() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null, null);

        assertTrue(datasets.size() == testPageSize);

        Integer datasetId = datasets.get(random.nextInt(datasets.size())).getDatasetId();

        Dataset dataset = datasetDao.getDatasetById(datasetId);

        assertTrue("Failing get Dataset by Id",
                dataset.getDatasetId() == datasetId);

    }

    @Test
    public void testGetDatasetByExperiemntId() {

        Integer testPageSize = 100;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null, null);


        assertTrue(datasets.size() <= testPageSize);

        Integer experimentId = datasets.get(random.nextInt(datasets.size())).getExperiment().getExperimentId();

        List<Dataset> datasetsByExperimentId = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                experimentId, null);

        for (Dataset dataset : datasetsByExperimentId) {
            assertTrue("Failing Experiment Id Filter",
                    dataset.getExperiment().getExperimentId() == experimentId);
        }

    }

    @Test
    public void testGetDatasetsWithAnalysisAndCounts() {

        Integer testPageSize = 100;
        Integer testRowOffset = 0;

        List<Object[]> resultTuple = datasetDao.getDatasetsWithAnalysesAndCounts(testPageSize, testRowOffset,
                null, null,
                null, null);


        assertTrue(resultTuple.size() <= testPageSize);

    }
}
