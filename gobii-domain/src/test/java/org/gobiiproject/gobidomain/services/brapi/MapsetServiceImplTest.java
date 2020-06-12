package org.gobiiproject.gobidomain.services.brapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiisampletrackingdao.MapsetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class MapsetServiceImplTest {

    @InjectMocks
    private MapsetServiceImpl mapsetBrapiService;

    @Mock
    private MapsetDaoImpl mapsetDao;

    MockSetup mockSetup;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockSetup = new MockSetup();
    }



    @Test
    public void getMapsTest() throws Exception {


    }


    @Test
    public void getMapById() throws Exception {


    }

}
