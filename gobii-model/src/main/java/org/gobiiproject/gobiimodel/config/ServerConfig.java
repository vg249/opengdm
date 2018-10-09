package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.dto.system.RestCallProfileDTO;
import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

@Root
public class ServerConfig {

    @Element(required = false)
    private boolean decrypt = false;

    @Element(required = false)
    private String userName = null;

    @Element(required = false)
    private String password = null;

    @Element(required = false)
    private ServerType serverType = null;

    @Element(required = false)
    private String host = "";

    @Element(required = false)
    private String contextPath = "";

    @Element(required = false)
    private Integer port = 0;

    @Element(required = false)
    private boolean isActive = false;

    public ServerConfig() {
    }

    public ServerConfig(ServerType serverType,
                        String host,
                        String contextPath,
                        Integer port,
                        boolean isActive,
                        String userName,
                        String password,
                        boolean decrypt,
                        EnumMap<RestRequestId, RestCallProfileDTO> callProfilesByRestRequestId) {

        this.serverType = serverType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;
        this.callProfilesByRestRequestId = callProfilesByRestRequestId;

    }

    public ServerConfig(ServerType serverType,
                        String host,
                        String contextPath,
                        Integer port,
                        boolean isActive,
                        boolean decrypt) {

        this.serverType = serverType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.decrypt = decrypt;

    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public ServerConfig setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
        return this;
    }

    public String getUserName() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.userName, null);
        } else {
            returnVal = this.userName;
        }

        return returnVal;
    }

    public ServerConfig setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.password, null);
        } else {
            returnVal = this.password;
        }

        return returnVal;
    }

    public ServerConfig setPassword(String password) {
        this.password = password;
        return this;
    }


    public ServerType getServerType() {
        return serverType;
    }

    public ServerConfig setServerType(ServerType serverType) {
        this.serverType = serverType;
        return this;
    }

    public ServerConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public ServerConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getPort() {
        return port;
    }


    public String getHost() {

        return host;
    }

    public boolean isActive() {
        return isActive;
    }

    public ServerConfig setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getContextPath() {
        return this.getContextPath(true);
    }

    public String getContextPath(boolean terminate) {

        String returnVal = this.contextPath;

        if (terminate && !LineUtils.isNullOrEmpty(returnVal)) {
            returnVal = LineUtils.terminateDirectoryPath(returnVal);
        }
        return returnVal;
    }

    public ServerConfig setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }


//    // These are in the general base class only temporarily until these KDC properties
//    // can be handled differently
//
//    public enum KDCResource {
//        QC_START,
//        QC_STATUS_,
//        QC_DOWNLOAD,
//        QC_PURGE
//    }
//
//    @ElementMap(required = false)
//    Map<ServerConfig.KDCResource, String> kdcResources = new HashMap<>();
//public ServerConfig addPath(ServerConfig.KDCResource kdcResource, String resource) {
//    this.kdcResources.put(kdcResource, resource);
//    return this;
//}
//
//    public String getPath(ServerConfig.KDCResource kdcResource) {
//
//        String returnVal = null;
//
//        if (this.kdcResources.containsKey(kdcResource)) {
//            returnVal = this.kdcResources.get(kdcResource);
//        }
//
//        return returnVal;
//    }
//

    @ElementMap(required = false)
    EnumMap<RestRequestId, RestCallProfileDTO> callProfilesByRestRequestId = new EnumMap<>(RestRequestId.class);

    @Element(required = false)
    Integer statusCheckIntervalSecs = 0;

    @Element(required = false)
    Integer maxStatusCheckMins = 0;


    public Integer getStatusCheckIntervalSecs() {
        return statusCheckIntervalSecs;
    }

    public ServerConfig setStatusCheckIntervalSecs(Integer statusCheckIntervalSecs) {
        this.statusCheckIntervalSecs = statusCheckIntervalSecs;
        return this;
    }

    public Integer getMaxStatusCheckMins() {
        return maxStatusCheckMins;
    }

    public ServerConfig setMaxStatusCheckMins(Integer maxStatusCheckMins) {
        this.maxStatusCheckMins = maxStatusCheckMins;
        return this;
    }


    List<ServerType> nonUpdatableServerTypes = Arrays.asList(ServerType.GOBII_WEB,
            ServerType.GOBII_PGSQL,
            ServerType.GOBII_COMPUTE);

    private RestCallProfileDTO getCallProfile(RestRequestId restRequestId) {

        if (!this.callProfilesByRestRequestId.containsKey(restRequestId)) {
            throw new GobiiException("There is no call profile for restRequestId " + restRequestId.getResourcePath());
        }

        return this.callProfilesByRestRequestId.get(restRequestId);
    }

    public Integer getCallMaxPost(RestRequestId restRequestId) {

        return this.getCallProfile(restRequestId).getMaxPostPut();
    }

    public Integer getCallGet(RestRequestId restRequestId) {

        return this.getCallProfile(restRequestId).getMaxGet();
    }

    public String getCallResourcePath(RestRequestId restRequestId) {
        return this.getCallProfile(restRequestId).getRestRequestId().getResourcePath();
    }

    public void setCallResourcePath(RestRequestId restRequestId, String resourcePath) throws GobiiException {


        if (this.nonUpdatableServerTypes.contains(restRequestId.getServerType())) {
            throw new GobiiException("This server type does not allow dynamic configuration of resource paths: " + restRequestId.getServerType().toString());
        }

        this.getCallProfile(restRequestId).getRestRequestId().setResourcePath(resourcePath);
    }

}
