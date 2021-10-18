package org.gobiiproject.gobiidomain.services.gdmv3;


import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigSettings.class, KeycloakService.class})
@Ignore
public class CropServiceImplTest {


    @Test
    public void getCropsTest() throws Exception{
        
        ConfigSettings configSettings = new ConfigSettings();

        CropServiceImpl cropService = new CropServiceImpl();
        Whitebox.setInternalState(cropService, "configSettings", configSettings);



        List<GobiiCropConfig> cropConfigs = new ArrayList<>();

        // One Authorized crop
        GobiiCropConfig  cropConfig1 = new GobiiCropConfig();
        cropConfig1.setActive(true);
        cropConfig1.setGobiiCropType("test1");
        cropConfigs.add(cropConfig1);

        // One not authorized crop
        GobiiCropConfig  cropConfig2 = new GobiiCropConfig();
        cropConfig2.setActive(true);
        cropConfig2.setGobiiCropType("test2");
        cropConfigs.add(cropConfig2);

        when(configSettings.getActiveCropConfigs()).thenReturn(cropConfigs);

        List<String> authorizedUserTypes = new ArrayList<>(Arrays.asList("/test1/pi"));
        PowerMockito.mockStatic(KeycloakService.class);
        PowerMockito.when(KeycloakService.getUserGroups()).thenReturn(authorizedUserTypes);


        PagedResult<CropsDTO> cropsPagedResult = cropService.getCrops();

        // Check size is correct
        assert cropsPagedResult.getCurrentPageSize() == 2;

        // Check name and authorization for the first crop
        assert cropsPagedResult.getResult().get(0).getCropType() == "test1";
        assert cropsPagedResult.getResult().get(0).isUserAuthorized();

        // Check name and authorization for the second crop
        assert cropsPagedResult.getResult().get(1).getCropType() == "test2";
        assert !cropsPagedResult.getResult().get(1).isUserAuthorized();

    }


}
