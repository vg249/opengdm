package org.gobiiproject.gobiisampletrackingdao;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class V3ProjectDaoImplTest {

    @Autowired
    private ProjectDao v3ProjectDao;


    @Before
    public void init() {
        //TODO: insert mock data
    }

    @Test
    @Transactional
    public void testSimpleQuery() {
       assert v3ProjectDao != null;
       assert v3ProjectDao.getProjects(0, 1000) != null;
       //assert v3ProjectDao.getProjects(0, 1000).size() > 0;
    }

}



