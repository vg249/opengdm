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


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    Random random = new Random();

    ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unused")
    private List<Mapset> getMockMapsets(Integer listSize) {

        List<Mapset> returnVal = new ArrayList<>();


        for(int i = 0; i < listSize; i++) {

            Mapset mapset = new Mapset();

            mapset.setMarkerCount(100);

            returnVal.add(mapset);

        }

        return returnVal;
   }


    @Test
    public void testMainFieldsMapping() throws Exception {

        //final Integer pageSize = 1000;

    }


    @Test
    public void testAdditionalInfoMapping() throws Exception {

        //final Integer pageSize = 1000;

    }

}
