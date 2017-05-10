package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Root
public class ServerBase {


    @Element(required = false)
    private String serviceDomain;

    @Element(required = false)
    private String serviceAppRoot;

    @Element(required = false)
    private Integer servicePort;

    @Element(required = false)
    private boolean isActive;

    public ServerBase() {
    }

    public ServerBase(String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      boolean isActive) {

        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.isActive = isActive;

    }


    public ServerBase setServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
        return this;
    }

    public ServerBase setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
        return this;
    }

   public Integer getServicePort() {
        return servicePort;
    }


    public String getServiceDomain() {

        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public ServerBase setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getServiceAppRoot() {

        return LineUtils.terminateDirectoryPath(this.serviceAppRoot);
    }

    public ServerBase setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
        return this;
    }


}
