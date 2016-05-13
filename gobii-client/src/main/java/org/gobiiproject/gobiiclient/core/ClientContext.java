package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 5/13/2016.
 */
public final class ClientContext {


    Logger LOGGER = LoggerFactory.getLogger(TypedRestRequest.class);

    // configure as a singleton
    // this may not be effective if more thn one classloader is used
    private static ClientContext clientContext = null;
    public synchronized static ClientContext getInstance() throws Exception {

        if (null == clientContext) {
            clientContext = new ClientContext();
        }

        return clientContext;
    }


    private ClientContext() throws Exception {

        configSettings = new ConfigSettings();
        configSettings.setCurrentCropType(ConfigSettings.CropType.RICE); // default crop

    }

    public enum ProcessMode {Asynch, Block}

    private ProcessMode processMode = ProcessMode.Asynch;
    private String userToken = null;

    private ConfigSettings configSettings;
    private HttpCore httpCore = new HttpCore();

    public String getCurrentCropDomain() {
        return configSettings.getCurrentCropConfig().getServiceDomain();
    }

    public Integer getCurrentCropPort() {
        return configSettings.getCurrentCropConfig().getServicePort();
    }


    public List<ConfigSettings.CropType> getCropTypeTypes() {
        return new ArrayList<>(Arrays.asList(ConfigSettings.CropType.values()));
    }


    public ConfigSettings.CropType getCurrentClientCrop() {
        return configSettings.getCurrentCropType();
    }

    public void setCurrentClientCrop(ConfigSettings.CropType currentClientCrop) {
        configSettings.setCurrentCropType(currentClientCrop);
    }

    public boolean login(String userName, String password) {
        boolean returnVal = true;

        try {
            String authUrl = Urls.getRequestUrl(ControllerType.EXTRACTOR,
                    ServiceRequestId.URL_AUTH);
            userToken = httpCore.getTokenForUser(authUrl, userName, password);
        } catch (Exception e) {
            LOGGER.error("Authenticating", e);
        }

        return returnVal;
    }

    public String getUserToken() {
        return userToken;
    }


}
