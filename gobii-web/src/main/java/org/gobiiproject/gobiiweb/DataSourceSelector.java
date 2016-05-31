package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by Phil on 5/25/2016.
 */
public class DataSourceSelector extends AbstractRoutingDataSource {

    Logger LOGGER = LoggerFactory.getLogger(DataSourceSelector.class);

    @Override
    protected Object determineCurrentLookupKey() {

        String returnVal = GobiiCropType.TEST.toString(); // always give a reasonable value

        try {

            ConfigSettings configSettings = new ConfigSettings();
            returnVal = configSettings.getDefaultGobiiCropType().toString();

        } catch (Exception e) {

            LOGGER.error("Exception setting default crop type", e);
        }


        GobiiCropType gobiiCropType = cropRequestAnalyzer.getCropTypeFromHeaders();
        if (null != gobiiCropType) {
            returnVal = gobiiCropType.toString();

        } else {

            gobiiCropType = cropRequestAnalyzer.getCropTypeFromUri();

            if (null == gobiiCropType) {
                LOGGER.error("Unable to determine crop type from header or uri; setting crop type to "
                        + returnVal
                        + " database connectioins will be made accordingly");
            }
        }

        return returnVal;
    }
}
