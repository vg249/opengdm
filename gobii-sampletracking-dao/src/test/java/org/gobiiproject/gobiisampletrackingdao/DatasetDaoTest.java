package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Dataset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

/**
 * This tests are created with knowledge of exisiting data in api.gobii.org:gobii-dev database
 * TODO: Setup class to create required data in the test database needs to be completed in future.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Slf4j
public class DatasetDaoTest {

    @Autowired
    private DatasetDao datasetDao;

    Random random = new Random();

    @Test
    public void testListDatasets() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(
                testPageSize, testRowOffset,
                null, null,
                null,
                null, null);


        assertTrue(datasets.size() <= testPageSize);

    }


    @Test
    public void testListDatasetsWithPageSize() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null,
                null, null);

        assertTrue(datasets.size() == 10);

    }

    @Test
    public void testGetDatasetById() {

        Integer testPageSize = 10;
        Integer testRowOffset = 0;

        List<Dataset> datasets = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null,
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
                null,
                null, null);


        assertTrue(datasets.size() <= testPageSize);

        Integer experimentId = datasets.get(random.nextInt(datasets.size())).getExperiment().getExperimentId();

        List<Dataset> datasetsByExperimentId = datasetDao.getDatasets(testPageSize, testRowOffset,
                null, null,
                null,
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

    @Test
    public void testCountAnalysesArray() {
        int count = datasetDao.getDatasetCountWithAnalysesContaining(1);
        log.info("Count: " + count);
        assert count >= 0;
    }
}
