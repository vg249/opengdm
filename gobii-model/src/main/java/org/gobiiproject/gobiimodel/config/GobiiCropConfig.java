package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the web server configuration properties necessary for a given
 * crop. In addition, it contains CropDbConfig instances for the databae configurations
 * for the specific crop.
 */
@Root
public class GobiiCropConfig extends ServerBase {


    @Element(required = false)
    private String gobiiCropType;

    @ElementMap(required = false)
    private Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public GobiiCropConfig(String gobiiCropType,
                           String serviceDomain,
                           String serviceAppRoot,
                           Integer servicePort,
                           boolean isActive,
                           boolean decrypt) {

        super(serviceDomain,serviceAppRoot,servicePort,isActive);
        this.gobiiCropType = gobiiCropType;
    }

    public void setCropDbConfig(GobiiDbType gobiiDbType,
                                String host,
                                String dbName,
                                Integer port,
                                String userName,
                                String password) {

        CropDbConfig cropDbConfig = this.cropDbConfigsByDbType.get(gobiiDbType);
        if (cropDbConfig == null) {

            cropDbConfig = new CropDbConfig();
            this.cropDbConfigsByDbType.put(gobiiDbType, cropDbConfig);

        }

        cropDbConfig
                .setGobiiDbType(gobiiDbType)
                .setHost(host)
                .setDbName(dbName)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password);
    }

    public GobiiCropConfig setServiceDomain(String serviceDomain) {
        super.setServiceDomain(serviceDomain);
        return this;
    }

    public GobiiCropConfig setServicePort(Integer servicePort) {
        super.setServicePort(servicePort);
        return this;
    }

    public GobiiCropConfig setCropDbConfigsByDbType(Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType) {
        this.cropDbConfigsByDbType = cropDbConfigsByDbType;
        return this;
    }

    public GobiiCropConfig setActive(boolean active) {
        super.setActive(active);
        return this;
    }


    public GobiiCropConfig setServiceAppRoot(String serviceAppRoot) {
        super.setServiceAppRoot(serviceAppRoot);
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiCropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        cropDbConfigsByDbType.put(gobiiDbTypee, cropDbConfig);

    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        CropDbConfig returnVal = this.cropDbConfigsByDbType.get(gobiiDbType);
        return returnVal;
    } // getCropDbConfig()

    public Collection<CropDbConfig> getCropConfigs() {
        return this.cropDbConfigsByDbType.values();
    }

}
