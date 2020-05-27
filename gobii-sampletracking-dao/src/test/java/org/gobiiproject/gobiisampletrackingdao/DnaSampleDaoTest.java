package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class DnaSampleDaoTest {

    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Autowired
    private CvDao cvDao;

    @PersistenceContext
    protected EntityManager em;

    Integer testPageSize = 10;

    private DaoTestSetUp daoTestSetUp;

    Random random = new Random();

    @Before
    public void createTestData() {
        daoTestSetUp = new DaoTestSetUp(em, cvDao);
        daoTestSetUp.createTestDnaSamples(testPageSize);
        em.flush();
    }


    @Test
    public void testGetDnaSamples() {

        List<DnaSample> dnaSamples =
            dnaSampleDao.getDnaSamples(testPageSize, 0);

        assertTrue("Empty dnasample list",dnaSamples.size() > 0);
        assertTrue("dnaSamples result list size not equal to the page size",
                dnaSamples.size() == testPageSize);

    }

    @Test
    public void testGetDnaSampleByDnaSampleId() {

        Integer dnaSampleQueryId =
            daoTestSetUp
                .getCreatedDnaSamples()
                .get(random.nextInt(
                    daoTestSetUp.getCreatedDnaSamples().size()))
                .getDnaSampleId();

        assertNotNull("DnaSample Test Setup with null dnaSampleId",
            dnaSampleQueryId);

        DnaSample dnaSamplesByDnaSampleId =
            dnaSampleDao.getDnaSampleByDnaSampleId(dnaSampleQueryId);

        assertEquals("Get DanSample by dnaSampleId failed",
                dnaSamplesByDnaSampleId.getDnaSampleId(),
                dnaSampleQueryId);


    }

    @Test
    public void testGetDnaSamplesByGermplasmId() {


        Integer germplasmQueryId =
            daoTestSetUp
                .getCreatedGermplasms()
                .get(random.nextInt(daoTestSetUp.getCreatedGermplasms().size()))
                .getGermplasmId();

        assertNotNull("DnaSampleDao test setup failed." +
            "Null germplasmId", germplasmQueryId);

        int numOfDnaSamplesWithGermplasmIds = 0;

        for(DnaSample dnaSample : daoTestSetUp.getCreatedDnaSamples()) {
            if(dnaSample.getGermplasm().getGermplasmId() == germplasmQueryId) {
                numOfDnaSamplesWithGermplasmIds += 1;
            }
        }

        List<DnaSample> dnaSamplesByGermplasmId =
            dnaSampleDao.getDnaSamplesByGermplasmId(
                germplasmQueryId, testPageSize, 0);

        assertTrue("Filter by germplasmDbId failed",
                dnaSamplesByGermplasmId.size() ==
                    numOfDnaSamplesWithGermplasmIds);

        for(DnaSample dnaSampleResult : dnaSamplesByGermplasmId) {

            assertEquals("Filter by germplasmDbId failed.",
                    dnaSampleResult.getGermplasm().getGermplasmId(),
                    germplasmQueryId);
        }
    }

    @Test
    public void testGetDnaSamplesByGermplasmExternalCode() {

        String germplasmExternalCode =
            daoTestSetUp.getCreatedGermplasms()
            .get(random.nextInt(daoTestSetUp.getCreatedGermplasms().size()))
            .getExternalCode();

        assertNotNull("DnaSampleDao test setup failed." +
            "Null germplasmExternalCode.", germplasmExternalCode);

        int numOfDnaSamplesWithGermplasmExternalCodes = 0;

        for(DnaSample dnaSample : daoTestSetUp.getCreatedDnaSamples()) {
            if(dnaSample.getGermplasm().getExternalCode() ==
                germplasmExternalCode) {
                numOfDnaSamplesWithGermplasmExternalCodes += 1;
            }
        }

        List<DnaSample> dnaSamplesByGermplasmCode =
            dnaSampleDao.getDnaSamplesByGermplasmExternalCode(
                germplasmExternalCode, testPageSize, 0);

        assertTrue("Filter by germplasmExternalCode failed",
                dnaSamplesByGermplasmCode.size() ==
                    numOfDnaSamplesWithGermplasmExternalCodes);

        for(DnaSample dnaSampleResult : dnaSamplesByGermplasmCode) {

            assertEquals("Filter by germplasmExternalCode.",
                    dnaSampleResult.getGermplasm().getExternalCode(),
                    germplasmExternalCode);
        }
    }
}
