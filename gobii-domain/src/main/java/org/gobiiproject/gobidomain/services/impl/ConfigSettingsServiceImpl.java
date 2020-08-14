package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.system.DtoMapConfigSettings;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.gobiiproject.gobiimodel.types.GobiiProcessType.READ;

/**
 * Created by Phil on 6/10/2016.
 */

public class ConfigSettingsServiceImpl implements ConfigSettingsService {

    Logger LOGGER = LoggerFactory.getLogger(ConfigSettingsServiceImpl.class);


    @Autowired
    DtoMapConfigSettings dtoMapConfigSettings;


    @Override
    public ConfigSettingsDTO getConfigSettings() throws GobiiException {

        ConfigSettingsDTO returnVal = new ConfigSettingsDTO();


        returnVal = dtoMapConfigSettings.readSettings();

        returnVal.getAllowedProcessTypes().add(READ);

        return returnVal;
    }

}
