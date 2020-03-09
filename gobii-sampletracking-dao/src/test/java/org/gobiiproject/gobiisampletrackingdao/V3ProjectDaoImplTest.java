package org.gobiiproject.gobiisampletrackingdao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class V3ProjectDaoImplTest {

    @Autowired
    private V3ProjectDao v3ProjectDao;


    @Test
    @Transactional
    public void testSimpleQuery() {
       assert v3ProjectDao != null;

       assert v3ProjectDao.getProjects(0, 1000) != null;
       assert v3ProjectDao.getProjects(0, 1000).size() > 0;
    }

}



