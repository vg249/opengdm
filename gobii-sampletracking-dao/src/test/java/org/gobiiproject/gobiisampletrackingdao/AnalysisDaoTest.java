package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.*;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *  @Transactional mean if you annotate your test suite with it? Well it means
 *  that every test method in your suite is surrounded by an overarching
 *  Spring transaction. This transaction will be rolled back at the end of the
 *  test method regardless of it's outcome.
 *  https://www.marcobehler.com/2014/06/25/should-my-tests-be-transactional
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class AnalysisDaoTest {

    @Autowired
    private  CvDao cvDao;

    @Autowired
    private AnalysisDao analysisDao;

    @PersistenceContext
    protected EntityManager em;


    static final Integer testPageSize = 10;

    DaoTestSetUp daoTestSetUp;

    @Before
    public void createTestData() {

        daoTestSetUp = new DaoTestSetUp(em, cvDao);
        daoTestSetUp.createTestAnalyses(testPageSize);
        em.flush();
    }


    @Test
    public void getAnalysesByAnalysisIdsTest() {

        Set<Integer> createdAnalysisIds = new HashSet<>();

        for(Analysis analysis : daoTestSetUp.getCreatedAnalyses()) {
            createdAnalysisIds.add(analysis.getAnalysisId());
        }

        List<Analysis> analyses = analysisDao.getAnalysesByAnalysisIds(
                createdAnalysisIds);

        for(Analysis analysis : analyses) {
            assertTrue("Failed Get Analysis by Ids Dao. " +
                            "Not fetching a valid id",
                    createdAnalysisIds.contains(analysis.getAnalysisId()));
        }

    }




}
