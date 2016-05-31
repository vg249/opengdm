package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    }

    public enum ProcessMode {Asynch, Block}

    private ProcessMode processMode = ProcessMode.Asynch;
    private String userToken = null;

    private ConfigSettings configSettings;

    public String getCurrentCropDomain() {
        return configSettings.getCurrentCropConfig().getServiceDomain();
    }

    public String getCurrentCropAppRoot() {
        return configSettings.getCurrentCropConfig().getServiceAppRoot();
    }


    public Integer getCurrentCropPort() {
        return configSettings.getCurrentCropConfig().getServicePort();
    }


    public List<GobiiCropType> getCropTypeTypes() {
        return new ArrayList<>(Arrays.asList(GobiiCropType.values()));
    }


    public GobiiCropType getCurrentClientCropType() {
        return configSettings.getCurrentGobiiCropType();
    }

    public CropConfig getCurrentClientCropConfig() {
        return configSettings.getCropConfig(this.getCurrentClientCropType());
    }

    public void setCurrentClientCrop(GobiiCropType currentClientCrop) {
        configSettings.setCurrentGobiiCropType(currentClientCrop);
    }

    public GobiiCropType getDefaultCropType() {
        return configSettings.getCurrentGobiiCropType();
    }

    public boolean login(String userName, String password) throws Exception {
        boolean returnVal = true;

        try {
            String authUrl = Urls.getRequestUrl(ControllerType.EXTRACTOR,
                    ServiceRequestId.URL_AUTH);

            HttpCore httpCore = new HttpCore(this.getCurrentCropDomain(),
                    this.getCurrentCropPort());

            userToken = httpCore.getTokenForUser(authUrl, userName, password);
        } catch (Exception e) {
            LOGGER.error("Authenticating", e);
            throw new Exception(e);
        }

        return returnVal;
    }

    public String getUserToken() {
        return userToken;
    }


}
