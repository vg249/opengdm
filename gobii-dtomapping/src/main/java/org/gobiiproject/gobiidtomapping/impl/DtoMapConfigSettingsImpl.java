package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapConfigSettings;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 6/10/2016.
 */
public class DtoMapConfigSettingsImpl implements DtoMapConfigSettings {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapConfigSettingsImpl.class);


    @Override
    public ConfigSettingsDTO readSettings(ConfigSettingsDTO configSettingsDTO) {
        ConfigSettingsDTO returnVal = configSettingsDTO;

        try {
            ConfigSettings configSettings = new ConfigSettings();
            for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

               ServerConfig currentServerConfig = new ServerConfig(currentCropConfig.getServicePort(),
                        currentCropConfig.getServiceDomain(),
                        currentCropConfig.getServiceAppRoot());

                currentServerConfig
                        .getFileLocations()
                        .put(GobiiFileLocationTypes.EXTRACTORINSTRUCTION_FILES,currentCropConfig.getExtractorInstructionFilesDirectory());

                currentServerConfig
                        .getFileLocations()
                        .put(GobiiFileLocationTypes.LOADERINSTRUCTION_FILES,currentCropConfig.getLoaderInstructionFilesDirectory());

                currentServerConfig
                        .getFileLocations()
                        .put(GobiiFileLocationTypes.INTERMEDIATE_FILES,currentCropConfig.getIntermediateFilesDirectory());

                currentServerConfig
                        .getFileLocations()
                        .put(GobiiFileLocationTypes.RAWUSER_FILES,currentCropConfig.getRawUserFilesDirectory());

                returnVal.getServerConfigs().put(currentCropConfig.getGobiiCropType(),
                        currentServerConfig);

            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;
    }
}
