package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.entity.Germplasm;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.MapsetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class MapsetBrapiServiceImplTest {

    @InjectMocks
    private MapsetBrapiServiceImpl mapsetBrapiService;

    @Mock
    private MapsetDaoImpl mapsetDao;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    Random random = new Random();

    ObjectMapper mapper = new ObjectMapper();

    private List<Mapset> getMockMapsets(Integer listSize) {

        List<Mapset> returnVal = new ArrayList();


        for(int i = 0; i < listSize; i++) {


            Mapset mapset = new Mapset();

            mapset.setMarkerCount(100);

            returnVal.add(mapset);

        }



        return returnVal;
   }


    @Test
    public void testMainFieldsMapping() throws Exception {

        final Integer pageSize = 1000;


    }


    @Test
    public void testAdditionalInfoMapping() throws Exception {

        final Integer pageSize = 1000;

    }

}