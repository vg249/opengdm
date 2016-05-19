package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiCropType;

import java.util.HashMap;
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

    private Map<GobiiCropType, CropConfig> cropConfigs = new HashMap<>();

    private GobiiCropType currentGobiiCropType = GobiiCropType.RICE; // default crop

    private String emailSvrType;
    private String emailSvrDomain;
    private String emailSvrUser;
    private String emailSvrHashType;
    private String emailSvrPassword;


    public ConfigSettings() throws Exception {

        String currentPrefix = null;

        currentGobiiCropType = GobiiCropType.valueOf(configReader.getPropValue(PROP_NAME_WEB_SVR_INSTANCECROP).toUpperCase());
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
            GobiiCropType currentGobiiCropType = GobiiCropType.valueOf(cropTypeFromProp);

            cropConfigs.put(currentGobiiCropType, currentCropConfig);

        }


    } // ctor


    public CropConfig getCropConfig(GobiiCropType gobiiCropType) {

        CropConfig returnVal = null;
        returnVal = cropConfigs.get(gobiiCropType);

        return returnVal;
    }

    public CropConfig getCurrentCropConfig() {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(GobiiCropType currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;
    }

    public GobiiCropType getCurrentGobiiCropType() {
        return currentGobiiCropType;
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
