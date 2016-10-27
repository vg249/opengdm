package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapConfigSettings;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 6/10/2016.
 */
public class DtoMapConfigSettingsImpl implements DtoMapConfigSettings {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapConfigSettingsImpl.class);


    @Override
    public ConfigSettingsDTO readSettings() throws GobiiException{

        ConfigSettingsDTO returnVal = new ConfigSettingsDTO();

        try {
            ConfigSettings configSettings = new ConfigSettings();
            returnVal.setDefaultCrop(configSettings.getDefaultGobiiCropType());
            for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfig currentServerConfig = new ServerConfig(currentCropConfig);

                returnVal.getServerConfigs().put(currentCropConfig.getGobiiCropType(),
                        currentServerConfig);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }
}
