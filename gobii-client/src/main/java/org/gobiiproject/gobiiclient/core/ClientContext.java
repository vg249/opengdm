package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.ConfigSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 5/13/2016.
 */
public final class ClientContext {


    // configure as a singleton
    // this may not be effective if more thn one classloader is used
    public final static ClientContext INSTANCE = new ClientContext();

    private void ClientContext() throws Exception {

        configSettings = new ConfigSettings();

        for(ConfigSettings.CropType currentCropType : ConfigSettings.CropType.values()) {
            configSettings.setCurrentCropType(currentCropType);
            String currentCropDomain = configSettings.getCurrentCropConfig().getServiceDomain();
            String currentCropPort = configSettings.getCurrentCropConfig().getServicePort().toString();
            String currentBaseUrl = currentCropDomain + ":" + currentCropPort + "\\";
            baseUrlsByCrop.put(currentCropType,currentBaseUrl);

        }
    }

    public enum ProcessMode {Asynch, Block}

    private Map<ConfigSettings.CropType,String> baseUrlsByCrop = new HashMap<>();
    private ProcessMode processMode = ProcessMode.Asynch;
    private String userToken = null;
    private ConfigSettings configSettings;
    private ConfigSettings.CropType currentClientCrop = ConfigSettings.CropType.RICE;


    public String getBaseUrl(ConfigSettings.CropType cropType) {
        return  baseUrlsByCrop.get(cropType);
    }

    public List<ConfigSettings.CropType> getCropTypeTypes() {
        return new ArrayList<>(Arrays.asList(ConfigSettings.CropType.values()));
    }


    public ConfigSettings.CropType getCurrentClientCrop() {
        return currentClientCrop;
    }

    public void setCurrentClientCrop(ConfigSettings.CropType currentClientCrop) {
        this.currentClientCrop = currentClientCrop;
    }

//    login(String userName, String password ) {
//        TypedRestRequest
//        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());
//
//    }

}
