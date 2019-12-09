package org.gobiiproject.gobiisampletrackingdao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Project;
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
    public void listDatasets() {

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(null, null, null);


        assertTrue(datasets.size() > 0);


    }



}
