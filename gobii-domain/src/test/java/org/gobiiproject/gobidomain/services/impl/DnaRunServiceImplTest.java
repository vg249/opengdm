package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by VCalaminos on 6/26/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class DnaRunServiceImplTest {

    @Autowired
    private DnaRunService dnaRunService = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        UserContextLoader userContextLoader = new UserContextLoader("classpath:/spring/test-config.xml");
        userContextLoader.loadUser("USER_READER");
    }

    @Test
    public void getDnaRunList() {

        List<DnaRunDTO> dnaRunDTOList;

        dnaRunDTOList = dnaRunService.getDnaRuns(null, null);

        if (dnaRunDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available dna runs in the database");
        }

        assertTrue(dnaRunDTOList.size() > 0);
        assertNotNull(dnaRunDTOList.get(0).getCallSetDbId());

    }

    @Test
    public void getDnaRunListWithPageSize() {

        List<DnaRunDTO> dnaRunDTOList;

        Integer pageSize = 1;

        dnaRunDTOList = dnaRunService.getDnaRuns(null, pageSize);

        if (dnaRunDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available dna runs in the database");
        }

        assertTrue(dnaRunDTOList.size() == pageSize);
        assertNotNull(dnaRunDTOList.get(0).getCallSetDbId());
    }

    @Test
    public void getDnaRunByDnaRunDbId() {

        DnaRunDTO dnaRunDTO = null;

        List<DnaRunDTO> dnaRunDTOList = dnaRunService.getDnaRuns(null, null);

        if (dnaRunDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available dna runs in the database");
        }

        Integer dnaRunDbId = dnaRunDTOList.get(0).getCallSetDbId();

        dnaRunDTO = dnaRunService.getDnaRunById(dnaRunDbId);

        assertTrue(dnaRunDTO.getCallSetDbId() > 0);
        assertTrue(dnaRunDTO.getCallSetDbId().equals(dnaRunDbId));
        assertNotNull(dnaRunDTO.getCallSetName());

    }

    @Test
    public void getDnaRunWithNonExistingId() {

        DnaRunDTO dnaRunDTO = new DnaRunDTO();

        try {

            // non-existing ID
            Integer dnaRunDbId = 0;

            dnaRunDTO = dnaRunService.getDnaRunById(dnaRunDbId);

        } catch (GobiiException gE) {

            assertTrue(gE.getGobiiValidationStatusType() == GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST);
            assertFalse(dnaRunDTO.getCallSetDbId() > 0);
        }

    }
}
