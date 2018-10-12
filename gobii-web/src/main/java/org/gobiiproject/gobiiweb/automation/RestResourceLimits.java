package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.rest.RestProfileDTO;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;

public class RestResourceLimits {


    /***
     * If a limit for the call exists, returns the limit; otherwise returns null
     * @param restResourceId
     * @param restMethodType
     * @param templateParameter
     * @return
     */
    public static Integer getCallLimit(RestResourceId restResourceId,
                                       RestMethodType restMethodType,
                                       String templateParameter) throws Exception {
        Integer returnVal = null;

        ServerConfig serverConfigWeb = (new ConfigSettings())
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);

        if (serverConfigWeb.isCallProfileDefined(restResourceId,
                restMethodType,
                templateParameter)) {

            returnVal = serverConfigWeb.getRestResourceLimit(
                    restResourceId,
                    restMethodType,
                    templateParameter.toUpperCase());
        }

        return returnVal;

    } //getCallLimit()


    /***
     * If a limit for the call exists, returns the limit; otherwise returns null
     * @param restResourceId
     * @param restMethodType
     * @return
     */
    public static Integer getCallLimit(RestResourceId restResourceId,
                                       RestMethodType restMethodType) throws Exception {
        Integer returnVal = null;

        ServerConfig serverConfigWeb = (new ConfigSettings())
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);

        if (serverConfigWeb.isCallProfileDefined(restResourceId,
                restMethodType)) {

            returnVal = serverConfigWeb.getRestResourceLimit(
                    restResourceId,
                    restMethodType);
        }

        return returnVal;

    } //getCallLimit()

    public static void setCallLimit(RestProfileDTO restProfileDTO) throws Exception {

        ConfigSettings configSettings = new ConfigSettings();
        ServerConfig serverConfigWeb = configSettings
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);


        if (LineUtils.isNullOrEmpty(restProfileDTO.getTemplateParameter())) {

            if (!serverConfigWeb
                    .isCallProfileDefined(restProfileDTO.getRestResourceId(),
                            restProfileDTO.getRestMethodType())) {

                throw new GobiiException("There is no rest resource profile defined for "
                        + restProfileDTO.getRestResourceId().getResourcePath()
                        + " (" + restProfileDTO.getRestMethodType().toString() + ")"
                );

            } else {
                serverConfigWeb.setRestResourceLimit(
                        restProfileDTO.getRestResourceId(),
                        restProfileDTO.getRestMethodType(),
                        restProfileDTO.getMax());

            } // if-else the profile is defined

        } else {

            if (!serverConfigWeb
                    .isCallProfileDefined(restProfileDTO.getRestResourceId(),
                            restProfileDTO.getRestMethodType(),
                            restProfileDTO.getTemplateParameter())) {

                throw new GobiiException("There is no rest resource profile defined for "
                        + restProfileDTO.getRestResourceId().getResourcePath()
                        + "/"
                        + restProfileDTO.getTemplateParameter()
                        + " (" + restProfileDTO.getRestMethodType().toString() + ")"
                );

            } else {
                serverConfigWeb.setRestResourceLimit(
                        restProfileDTO.getRestResourceId(),
                        restProfileDTO.getRestMethodType(),
                        restProfileDTO.getTemplateParameter(),
                        restProfileDTO.getMax());

            } // if-else the profile is defined

        } // if-else there is a template parameter

        configSettings.commit();

    } //set call limit
}
