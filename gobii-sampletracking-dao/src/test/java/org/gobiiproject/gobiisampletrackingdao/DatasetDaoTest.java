package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Dataset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class DatasetDaoTest {

    @Autowired
    private DatasetDao datasetDao;



    @Test
    public void testListDatasetsAnalyses() {

        List<Dataset> datasets = datasetDao.listDatasetsAnalyses();


        assertTrue(datasets.size() > 0);


    }

    @Test
    public void testListDatasets() {

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(null, null, null);


        assertTrue(datasets.size() > 0);


    }

    @Test
    public void testListDatasetsWithPageSize() {

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(0, 10, null);


        assertTrue(datasets.size() == 10);


    }


    @Test
    public void testListDatasetsByCursor() {

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(0, 10, null);

        assertTrue(datasets.size() == 10);

        Integer pageCursor = datasets.get(9).getDatasetId();

        List<Dataset> datasetsByPageCursor = datasetDao.listDatasetsByPageCursor(pageCursor.toString(), 5);

        assertTrue(datasetsByPageCursor.get(0).getDatasetId() > pageCursor);

        assertTrue(datasetsByPageCursor.size() == 5);

    }

    @Test
    public void testGetDatasetById() {

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(0, 10, null);

        assertTrue(datasets.size() == 10);

        Integer datasetId = datasets.get(9).getDatasetId();

        Dataset dataset = datasetDao.getDatasetById(datasetId);

        assertTrue(dataset.getDatasetId() == datasetId);


    }

}
