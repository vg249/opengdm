// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestConfigSettings {

    public ConfigSettingsDTO process(ConfigSettingsDTO configSettingsDTO) throws Exception {

        return new DtoRequestProcessor<ConfigSettingsDTO>().process(configSettingsDTO,
                ConfigSettingsDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CONFIGSETTINGS);

    } // getPingFromExtractController()

}
