package org.gobiiproject.gobidomain.services.gdmv3;


import org.gobiiproject.gobidomain.services.brapi.MockSetup;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebAppConfiguration
public class CropServiceImplTest {

    @InjectMocks
    private CropServiceImpl cropService;

    @Mock
    private ConfigSettings configSettings;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCropsTest() throws Exception{

        List<GobiiCropConfig> cropConfigs = new ArrayList<>();

        GobiiCropConfig  cropConfig1 = new GobiiCropConfig();

        cropConfig1.setActive(true);
        cropConfig1.setGobiiCropType("test1");

        cropConfigs.add(cropConfig1);

        when(configSettings.getActiveCropConfigs()).thenReturn(cropConfigs);

        List<CropsDTO> cropsDTOS = cropService.getCrops();

        assert cropsDTOS.size() == 1;

        assert cropsDTOS.get(0).getCropType() == "test1";
    }
}
