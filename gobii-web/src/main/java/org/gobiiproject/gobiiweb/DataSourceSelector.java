package org.gobiiproject.gobiiweb;

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

        return UrlAnalyzier.findCurrentCropType().toString();

    }
}
