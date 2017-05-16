package org.gobiiproject.gobiiclient.core.gobii;

import org.gobiiproject.gobiiclient.core.common.HttpCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class GobiiRestResourceUtils {

    Logger LOGGER = LoggerFactory.getLogger(GobiiRestResourceUtils.class);

    private HttpCore httpCore = null;
    private GobiiClientContext gobiiClientContext;

    public GobiiRestResourceUtils() {
    }


    public GobiiClientContext getGobiiClientContext() throws Exception {

        if (!GobiiClientContext.isInitialized()) {
            throw new Exception("Client context is not initialized");
        }

        gobiiClientContext = GobiiClientContext.getInstance(null, false);

        return gobiiClientContext;

    }


}