package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Phil on 5/5/2016.
 */
interface ConfigValues {

    CropConfig getCropConfig(GobiiCropType gobiiCropType);

    List<CropConfig> getActiveCropConfigs();

    CropConfig getCurrentCropConfig();


    void setCurrentGobiiCropType(GobiiCropType currentGobiiCropType);

    GobiiCropType getCurrentGobiiCropType();

    GobiiCropType getDefaultGobiiCropType();

    void setDefaultGobiiCropType(GobiiCropType defaultGobiiCropType) ;

    String getEmailSvrPassword();

    String getEmailSvrHashType();

    String getEmailSvrUser();

    String getEmailSvrDomain();

    String getEmailSvrType();

    Integer getEmailServerPort();

    void setEmailServerPort(Integer emailServerPort);

    boolean isIflIntegrityCheck();

    void setIflIntegrityCheck(boolean iflIntegrityCheck);

    String getFileSystemRoot();
}
