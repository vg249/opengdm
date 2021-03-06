package org.gobiiproject.gobiidtomapping.system.impl;

import org.gobiiproject.gobiidtomapping.system.DtoMapConfigSettings;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileNoticeType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 6/10/2016.
 */
public class DtoMapConfigSettingsImpl implements DtoMapConfigSettings {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapConfigSettingsImpl.class);


    @Override
    public ConfigSettingsDTO readSettings() throws GobiiException {

        ConfigSettingsDTO returnVal = new ConfigSettingsDTO();

        try {
            ConfigSettings configSettings = new ConfigSettings();

            returnVal.setMaxUploadSizeMbytes(configSettings.getMaxUploadSizeMbytes());

            returnVal.setServerCapabilities(configSettings.getServerCapabilities());

            for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfigItem currentServerConfigItem = new ServerConfigItem(currentGobiiCropConfig,
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.RAW_USER_FILES),
                        configSettings.getFileNoticePath(currentGobiiCropConfig.getGobiiCropType(), GobiiFileNoticeType.CONFIDENTIALITY)
                );


                returnVal.getServerConfigs().put(currentGobiiCropConfig.getGobiiCropType(),
                        currentServerConfigItem);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }
}
