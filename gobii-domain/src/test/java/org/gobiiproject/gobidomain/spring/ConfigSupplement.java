package org.gobiiproject.gobidomain.spring;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 8/16/2016.
 */

@Configuration
public class ConfigSupplement {

    private static GobiiCropConfig testExecConfig = null;

    private static String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

    @Bean(name="dataSourceMulti")
    public DataSourceSelector dataSourceMulti() throws Exception {

        DataSourceSelector returnVal = new DataSourceSelector();

        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);

        if (configFileLocation == null) {
            String message = "The the environment does not define the FQPN of " +
                    "configuration in environment variable: " + CONFIG_FILE_LOCATION_PROP;
            throw new Exception(message);
        }

        ConfigSettings configSettings = new ConfigSettings(configFileLocation);

        Map<Object,Object> targetDataSources = new HashMap<>();
        for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

            ServerConfig currentPostGresConfig = currentGobiiCropConfig.getServer(ServerType.GOBII_PGSQL);
            DriverManagerDataSource currentDataSource = new DriverManagerDataSource();

            currentDataSource.setDriverClassName("org.postgresql.Driver");

            String url = HelperFunctions.getJdbcConnectionString(currentPostGresConfig);
            currentDataSource.setUrl(url);
            currentDataSource.setUsername(currentPostGresConfig.getUserName());
            currentDataSource.setPassword(currentPostGresConfig.getPassword());

            targetDataSources.put(currentGobiiCropConfig.getGobiiCropType(),currentDataSource);

        } // iterate crop configs

        returnVal.setTargetDataSources(targetDataSources);

        return returnVal;

    }


}
