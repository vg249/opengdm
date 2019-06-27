package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
        UserContextLoader userContextLoader = new UserContextLoader();
        userContextLoader.loadUser("USER_READER");
    }

    @Test
    public void getDnaRunList() {

        List<DnaRunDTO> dnaRunDTOList = new ArrayList<>();

        dnaRunDTOList = dnaRunService.getDnaRuns(null, null);

    }

    @Test
    public void getDnaRunListWithPageSize() {

        List<DnaRunDTO> dnaRunDTOList = new ArrayList<>();

        Integer pageSize = 1;

        dnaRunDTOList = dnaRunService.getDnaRuns(null, pageSize);

        Assert.assertTrue(dnaRunDTOList.size() == pageSize);
    }
}
