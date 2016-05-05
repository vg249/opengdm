package org.gobiiproject.gobiimodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Phil on 5/5/2016.
 */
public class ConfigSettings {


    private final String PROP_NAME_WEB_SVR_INSTANCECROP = "websvr.instancecrop";
    private final String PROP_NAME_MAIL_SVR_TYPE = "mailsvr.type";
    private final String PROP_NAME_MAIL_SVR_DOMAIN = "mailsvr.domain";
    private final String PROP_NAME_MAIL_SVR_USER = "mailsvr.user";
    private final String PROP_NAME_MAIL_SVR_HASHTYPE = "mailsvr.hashtype";
    private final String PROP_NAME_MAIL_SVR_PWD = "mailsvr.pwd";

    private final String CROP_SUFFIX_SERVICE_DOMAIN = "servicedomain";
    private final String CROP_SUFFIX_SERVICE_PORT = "serviceport";
    private final String CROP_SUFFIX_USER_FILE_LOCLOCATION = "usrfloc";
    private final String CROP_SUFFIX_LOADR_FILE_LOCATION = "ldrfloc";
    private final String CROP_SUFFIX_INTERMEDIATE_FILE_LOCATION = "intfloc";

    private final String CROP_PREFIX = "crops.";
    private final String CROP_PEFIX_RICE = CROP_PREFIX + "rice.";
    private final String CROP_PEFIX_WHEAT = CROP_PREFIX + "wheat.";
    private final String CROP_PEFIX_MAIZE = CROP_PREFIX + "maize.";
    private final String CROP_PEFIX_SORGUM = CROP_PREFIX + "sorgum.";
    private final String CROP_PEFIX_CHICKPEA = CROP_PREFIX + "chickpea.";

    private String[] cropPrefixes = {
            CROP_PEFIX_RICE,
            CROP_PEFIX_WHEAT,
            CROP_PEFIX_MAIZE,
            CROP_PEFIX_SORGUM,
            CROP_PEFIX_CHICKPEA
    };

    private ConfigFileReader configReader = new ConfigFileReader();

    public enum CropType {RICE, WHEAT, MAIZE, SORGUM, CHICKPEA}

    private Map<CropType, CropConfig> cropConfigs = new HashMap<>();

    private CropType currentCropType = CropType.RICE; // default crop

    private String emailSvrType;
    private String emailSvrDomain;
    private String emailSvrUser;
    private String emailSvrHashType;
    private String emailSvrPassword;


    public ConfigSettings() throws Exception {

        String currentPrefix = null;

        currentCropType = CropType.valueOf(configReader.getPropValue(PROP_NAME_WEB_SVR_INSTANCECROP).toUpperCase());
        emailSvrType = configReader.getPropValue(PROP_NAME_MAIL_SVR_TYPE);
        emailSvrDomain = configReader.getPropValue(PROP_NAME_MAIL_SVR_DOMAIN);
        emailSvrUser = configReader.getPropValue(PROP_NAME_MAIL_SVR_USER);
        emailSvrHashType = configReader.getPropValue(PROP_NAME_MAIL_SVR_HASHTYPE);
        emailSvrPassword = configReader.getPropValue(PROP_NAME_MAIL_SVR_PWD);


        for (int idx = 0; idx < cropPrefixes.length; idx++) {
            currentPrefix = cropPrefixes[idx];

            String serviceDomain = configReader.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_DOMAIN);
            Integer servicePort = Integer.parseInt(configReader.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_PORT));
            String userFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_USER_FILE_LOCLOCATION);
            String loaderFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_LOADR_FILE_LOCATION);
            String intermediateFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_INTERMEDIATE_FILE_LOCATION);

            CropConfig currentCropConfig = new CropConfig(serviceDomain,
                    servicePort,
                    userFilesLocation,
                    loaderFilesLocation,
                    intermediateFilesLocation);


            String cropTypeFromProp = currentPrefix
                    .replace(CROP_PREFIX, "")
                    .replace(".","")
                    .toUpperCase();
            CropType currentCropType = CropType.valueOf(cropTypeFromProp);

            cropConfigs.put(currentCropType, currentCropConfig);

        }


    } // ctor


    public CropConfig getCropConfig(CropType cropType) {

        CropConfig returnVal = null;
        returnVal = cropConfigs.get(cropType);

        return returnVal;
    }

    public CropConfig getCurrentCropConfig() {
        return getCropConfig(getCurrentCropType());
    }


    public void setCurrentCropType(CropType currentCropType) {
        this.currentCropType = currentCropType;
    }

    public CropType getCurrentCropType() {
        return currentCropType;
    }

    public String getEmailSvrPassword() {
        return emailSvrPassword;
    }

    public String getEmailSvrHashType() {
        return emailSvrHashType;
    }

    public String getEmailSvrUser() {
        return emailSvrUser;
    }

    public String getEmailSvrDomain() {
        return emailSvrDomain;
    }

    public String getEmailSvrType() {
        return emailSvrType;
    }

}
