package org.gobiiproject.gobiiweb.spring;

import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiiweb.DataSourceSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 8/16/2016.
 */

@Configuration
public class ConfigSupplement {

    @Autowired
    private ThreadLocal<HttpServletRequest> currentRequest;

    private static String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

    private ConfigSettings configSettings;

    public ConfigSupplement() {
        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);
        this.configSettings = new ConfigSettings(configFileLocation);
    }

    @Bean(name="dataSourceMulti")
    public DataSourceSelector dataSourceMulti() throws Exception {

        DataSourceSelector returnVal = new DataSourceSelector();

        returnVal.setCurrentRequest(currentRequest);


        Map<Object,Object> targetDataSources = new HashMap<>();

        for (GobiiCropConfig currentGobiiCropConfig : this.configSettings.getActiveCropConfigs()) {

            //Sets Postgres settings
            ServerConfig currentPostGresConfig = currentGobiiCropConfig.getServer(ServerType.GOBII_PGSQL);

            DriverManagerDataSource currentDataSource = new DriverManagerDataSource();

            currentDataSource.setDriverClassName("org.postgresql.Driver");

            String url = HelperFunctions.getJdbcConnectionString(currentPostGresConfig);

            currentDataSource.setUrl(url);
            currentDataSource.setUsername(currentPostGresConfig.getUserName());
            currentDataSource.setPassword(currentPostGresConfig.getPassword());

            targetDataSources.put(currentGobiiCropConfig.getGobiiCropType(),currentDataSource);

            //Sets HDF5 Settings.
            //HDF5Interface.setPathToHDF5Files(
            //        currentGobiiCropConfig.getGobiiCropType(), "");


        } // iterate crop configs

        returnVal.setTargetDataSources(targetDataSources);

        return returnVal;

    }

    @Bean(name="pathToHdf5Exe")
    public String PathToHdf5Executables() {
        return this.configSettings.get
    }

    @Bean(name="extractorOutputPath")
    public String Hdf5FilesPath() {

        HDF5Interface.setPathToHDF5(this.configSettings.gethdf5ExePath());



        return "";
    }


}
