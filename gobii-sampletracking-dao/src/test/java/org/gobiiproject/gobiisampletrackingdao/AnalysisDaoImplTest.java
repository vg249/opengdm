package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/test-config.xml" })
@Slf4j
public class AnalysisDaoImplTest {

    @Autowired
    private AnalysisDao analysisDao;

    @Test
    public void testListAnalysis() {
        log.info("Test listing");
        List<Analysis> results = analysisDao.getAnalyses(0, 10);
        assert results != null;
        log.debug("Size: " + results.size());
        assert results.size() > 0;

    }

}