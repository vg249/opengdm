package org.gobiiproject.gobiimodel.config;


import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.gobiiproject.gobiimodel.dto.rest.RestResourceProfile;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * This class contains the web server configuration properties necessary for a given
 * crop. In addition, it contains GobiiCropDbConfig instances for the databae configurations
 * for the specific crop.
 */
@Root
public class GobiiCropConfig {

    public GobiiCropConfig(String cropType, boolean isActive) {
        this.gobiiCropType = cropType;
        this.isActive = isActive;
    }

    @Element(required = false)
    private String gobiiCropType;

    @Element(required = false)
    private boolean isActive = true;

    @ElementMap(required = false)
    private Map<ServerType, ServerConfig> serversByServerType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public void addServer(ServerType serverType,
                          String host,
                          String contextPath,
                          Integer port,
                          String userName,
                          String password,
                          boolean decrypt,
                          EnumMap<RestResourceId, RestResourceProfile> callProfilesByRestRequestId) {


        ServerConfig serverConfig = this.serversByServerType.get(serverType);
        if (serverConfig == null) {

            serverConfig = new ServerConfig();
            this.serversByServerType.put(serverType, serverConfig);

        }

        serverConfig
                .setServerType(serverType)
                .setHost(host)
                .setContextPath(contextPath)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password)
                .setDecrypt(decrypt)
                .setActive(this.isActive)
                .setResourceProfilesByRestRequestId(callProfilesByRestRequestId);
    }

    public GobiiCropConfig setServersByServerType(Map<ServerType, ServerConfig> serversByServerType) {
        this.serversByServerType = serversByServerType;
        return this;
    }

    public ServerConfig getServer(ServerType serverType) {
        ServerConfig returnVal = this.serversByServerType.get(serverType);
        return returnVal;
    } // getServer()

    public Collection<ServerConfig> getServers() {
        return this.serversByServerType.values();
    }


    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiCropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }


    /***
     * For now, we will assume that the child servers active status goes along with that
     * of the crop (parent) instance (web, postgres, and compute node). This may need to change
     * someday.
     * @return
     */
    public boolean isActive() {

        boolean returnVal = this.isActive;

        if (this.isActive) {
            for (ServerConfig currentServerConfig : this.getServers()) {
                if (!currentServerConfig.isActive()) {
                    returnVal = false;
                    break;
                }
            }
        }

        return returnVal;
    }

    public GobiiCropConfig setActive(boolean active) {
        isActive = active;

        for (ServerConfig currentServerConfig : this.getServers()) {
            currentServerConfig.setActive(active);
        }

        return this;
    }
}
