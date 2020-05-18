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
import javax.xml.crypto.Data;
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

    DaoTestSetUp daoTestSetUp;

    Random random = new Random();

    final Integer testPageSize = 10;

    @Before
    public void createTestData() {
        daoTestSetUp = new DaoTestSetUp(em, cvDao);
        daoTestSetUp.createTestDatasets(testPageSize);
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

        Integer datasetId =
            daoTestSetUp
                .getCreatedDatasets()
                .get(random.nextInt(daoTestSetUp.getCreatedDatasets().size()))
                .getDatasetId();

        Dataset dataset = datasetDao.getDatasetById(datasetId);

        assertTrue("Failing get Dataset by Id",
                dataset.getDatasetId() == datasetId);

    }

    @Test
    public void testGetDatasetByExperiemntId() {

        Integer experimentId =
            daoTestSetUp.getCreatedExperiments()
                .get(
                    random.nextInt(daoTestSetUp.getCreatedExperiments().size()))
                .getExperimentId();

        List<Dataset> datasetsByExperimentId =
            datasetDao.getDatasets(testPageSize, 0,
                null, null,
                experimentId, null);

        int numOfDatasetsInExperiemt = 0;

        for(Dataset dataset : daoTestSetUp.getCreatedDatasets()) {
            if(dataset.getExperiment().getExperimentId() == experimentId) {
                numOfDatasetsInExperiemt++;
            }
        }

        assertTrue("No Dataset to test",
            datasetsByExperimentId.size() <= testPageSize
            && datasetsByExperimentId.size() == numOfDatasetsInExperiemt);

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
