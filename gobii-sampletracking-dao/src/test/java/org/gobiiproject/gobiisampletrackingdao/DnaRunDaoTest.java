package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.*;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class DnaRunDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DnaRunDao dnaRunDao;

    @Autowired
    private CvDao cvDao;

    final int testPageSize = 10;

    Random random = new Random();

    DaoTestSetUp daoTestSetUp;

    @Before
    public void testCreateData() {
        daoTestSetUp = new DaoTestSetUp(em, cvDao);
        daoTestSetUp.createTestDnaRuns(testPageSize);
        em.flush();
    }

    @Test
    public void testGetDnaRuns() {


        Integer rowOffset = 0;

        List<DnaRun> dnaruns = dnaRunDao.getDnaRuns(
                testPageSize, rowOffset,
                null, null,
                null, null,
                null, null,
                null, null);

        assertTrue("Empty dnaRun list: ",dnaruns.size() > 0);

        if(dnaruns.size() > 0) {

            int pageSize = dnaruns.size() - 1;

            List<DnaRun> dnaRunsPaged = dnaRunDao.getDnaRuns(
                    pageSize, rowOffset,
                    null, null,
                    null, null,
                    null, null,
                    null, null);


            assertTrue("dnarun result list size not equal to the page size",
                    dnaRunsPaged.size() == pageSize);
        }
    }

    @Test
    public void testGetDnaRunsByDatasetIdWithNoAssociations() {

        Integer rowOffset = 0;
        Integer datasetId = 9;

        List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDatasetId(
                datasetId, testPageSize,
                rowOffset, false);

        assertTrue("Empty dnaRun list: ", dnaRuns.size() > 0);

        for(DnaRun dnaRun : dnaRuns) {
            assertTrue("Filter by datasetId failed",
                dnaRun.getDatasetDnaRunIdx().has(datasetId.toString()));
        }
    }

    @Test
    public void testGetDnaRunsFilterByGermplasmFields() {

        Integer rowOffset = 0;
        String germplasmName = daoTestSetUp.getCreatedGermplasms()
            .get(random.nextInt(daoTestSetUp.getCreatedGermplasms().size()))
            .getGermplasmName();

        List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(testPageSize, rowOffset,
                null, null,
                null, null,
                null, null,
                null, germplasmName);

        //there is only one dnarun for this germplasm
        assertTrue("DnaRunDao test setup for germplasm filter not working",
            dnaRuns.size() <= testPageSize && dnaRuns.size() > 0);

        for(DnaRun dnaRun : dnaRuns) {
            assertTrue("Filter By Germplasm Name Failed",
                germplasmName ==
                    dnaRun.getDnaSample().getGermplasm().getGermplasmName());
        }


        Integer germplasmId = daoTestSetUp.getCreatedGermplasms()
            .get(random.nextInt(daoTestSetUp.getCreatedGermplasms().size()))
            .getGermplasmId();

        List<DnaRun> dnaRunsByGermplasmId =
            dnaRunDao.getDnaRuns(testPageSize, rowOffset,
                null, null,
                null, null,
                null, null,
                germplasmId, null);

        //there is only one dnarun for this germplasm
        assertTrue("DnaRunDao test setup for germplasm filter not working",
            dnaRuns.size() <= testPageSize && dnaRuns.size() > 0);

        for(DnaRun dnaRun : dnaRunsByGermplasmId) {
            assertTrue("Filter By Germplasm Id Failed",
                germplasmId ==
                    dnaRun.getDnaSample().getGermplasm().getGermplasmId());
        }
    }

    @Test
    public void testGetDnaRunsFilterByExperimentFields() {

        Integer rowOffset = 0;
        Integer experimentId = daoTestSetUp.getCreatedExperiments()
            .get(random.nextInt(daoTestSetUp.getCreatedExperiments().size()))
            .getExperimentId();

        List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(testPageSize, rowOffset,
            null, null,
            null, experimentId,
            null, null,
            null, null);

        //there is only one dnarun for this germplasm
        assertTrue("DnaRunDao test setup for germplasm filter not working",
            dnaRuns.size() <= testPageSize && dnaRuns.size() > 0);

        for(DnaRun dnaRun : dnaRuns) {
            assertTrue("Filter By Experiment Failed",
                experimentId ==
                    dnaRun.getExperiment().getExperimentId());
        }

    }

    @Test
    public void testGetDnaRunsByDatasetId() {


        Integer rowOffset = 0;

        List<DnaRun> dnaruns = dnaRunDao.getDnaRuns(
                testPageSize, rowOffset,
                null, null,
                null, null,
                null, null,
                null, null);

        assertTrue("Empty dnaRun list: ",dnaruns.size() > 0);

        Integer dnaRunWithDatasetIndex = 0;

        while(
            (dnaruns.get(dnaRunWithDatasetIndex).getDatasetDnaRunIdx() == null
                || dnaruns
                    .get(dnaRunWithDatasetIndex)
                    .getDatasetDnaRunIdx().size() == 0)
                && dnaRunWithDatasetIndex < dnaruns.size()) {

            dnaRunWithDatasetIndex++;

        }

        assertTrue("No DnaRuns with a dataset mapped to it",
                dnaRunWithDatasetIndex < dnaruns.size());

        if(dnaRunWithDatasetIndex < dnaruns.size()) {


            Integer datasetId = Integer.parseInt(
                    dnaruns.get(dnaRunWithDatasetIndex)
                        .getDatasetDnaRunIdx().fieldNames().next());

            List<DnaRun> dnarunsByDatasetId = dnaRunDao.getDnaRunsByDatasetId(
                    datasetId, testPageSize, rowOffset);

            assertTrue("Empty dnarun list for given dataset id",
                dnarunsByDatasetId.size() > 0);

            for(DnaRun dnaRun : dnarunsByDatasetId) {
                assertTrue("Fetch by dataset id not working",
                        dnaRun.getDatasetDnaRunIdx().has(datasetId.toString()));
            }

        }


    }

    @Test
    public void getDnaRunsByDnaRunIds() {

        HashSet<Integer> dnarunIdSet = new HashSet<>();

        for(DnaRun dnaRun : daoTestSetUp.getCreatedDnaRuns()) {
            dnarunIdSet.add(dnaRun.getDnaRunId());
        }

        List<DnaRun> dnaruns = dnaRunDao.getDnaRunsByDanRunIds(dnarunIdSet);

        assertTrue("Failed DnaRuns Test Setup",
            dnaruns.size() <= dnarunIdSet.size()
            && dnaruns.size() > 0);

        for(DnaRun dnaRun : dnaruns) {
            assertTrue("Failed DnaRun filter by Ids",
                dnarunIdSet.contains(dnaRun.getDnaRunId()));
        }

    }


    @Test
    public void getDnaRunsByDnaRunNames() {

        Set<String> dnarunNameSet = new HashSet<>();

        for(DnaRun dnaRun : daoTestSetUp.getCreatedDnaRuns()) {
            dnarunNameSet.add(dnaRun.getDnaRunName());
        }

        List<DnaRun> dnaruns = dnaRunDao.getDnaRunsByDanRunNames(dnarunNameSet);

        assertTrue("Failed DnaRuns Test Setup",
            dnaruns.size() <= dnarunNameSet.size()
                && dnaruns.size() > 0);

        for(DnaRun dnaRun : dnaruns) {
            assertTrue("Failed DanRun filter by dnarun names",
                dnarunNameSet.contains(dnaRun.getDnaRunName()));
        }


    }


}
