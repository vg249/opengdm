package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class ConfigSettingsDTO extends DtoMetaData {


    private Map<String, ServerConfig> serverConfigs = new LinkedHashMap<>();

    public Map<String, ServerConfig> getServerConfigs() {
        return serverConfigs;
    }

    public void setServerConfigs(Map<String, ServerConfig> serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    String defaultCrop;

    public String getDefaultCrop() {
        return defaultCrop;
    }

    public void setDefaultCrop(String defaultCrop) {
        this.defaultCrop = defaultCrop;
    }
}
