package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class AnalysisDaoTest {

    @Autowired
    private  CvDao cvDao;

    @Autowired
    private AnalysisDao analysisDao;


    static final Integer testPageSize = 10;

    static Set<Integer> createdAnalysisIds = new HashSet<>();


    @BeforeClass
    public static void createTestData() {

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:/spring/test-config.xml");
        Random random = new Random();

        CvDao cvDao = context.getBean(CvDao.class);
        AnalysisDao analysisDao = context.getBean(AnalysisDao.class);

        List<Cv> analysisTypes = cvDao.getCvListByCvGroup(
                CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        assertTrue("System defined analysis type values are not found.",
                analysisTypes.size() > 0);

       Cv newStatus = cvDao.getCvs(
               "new",
               CvGroup.CVGROUP_STATUS.getCvGroupName(),
               GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

       for (int i = 0; i < testPageSize; i++) {

           Integer analysisTypeIndex = random.nextInt(analysisTypes.size());
           String analysisName = RandomStringUtils.random(7, true, true);

           Analysis analysis = new Analysis();

           analysis.setAnalysisName(analysisName);
           analysis.setType(analysisTypes.get(analysisTypeIndex));
           analysis.setStatus(newStatus);

           try {
               analysisDao.createAnalysis(analysis);
           }
           catch (Exception e) {
               TestCase.fail("Unknown Exception: "+ e.getMessage());
           }

           createdAnalysisIds.add(analysis.getAnalysisId());
       }

    }


    @Test
    public void getAnalysesByAnalysisIdsTest() {

        List<Analysis> analyses = analysisDao.getAnalysesByAnalysisIds(
                createdAnalysisIds);

        for(Analysis analysis : analyses) {
            assertTrue("Failed Get Analysis by Ids Dao. " +
                            "Not fetching a valid id",
                    createdAnalysisIds.contains(analysis.getAnalysisId()));
        }

    }


    @AfterClass
    public static void clearTestData() {

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:/spring/test-config.xml");

        AnalysisDao analysisDao = context.getBean(AnalysisDao.class);

        for(Integer analysisId : createdAnalysisIds) {
            try {
                Analysis analysis = new Analysis();
                analysis.setAnalysisId(analysisId);
                analysisDao.deleteAnalysis(analysis);
            }
            catch (Exception e) {
                TestCase.fail("Unknown Exception: "+ e.getMessage());
            }
        }
    }


}
