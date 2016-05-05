package org.gobiiproject.gobiimodel;

/**
 * Created by Phil on 5/5/2016.
 */
public class CropConfig {


    private String serviceDomain;
    private Integer servicePort;
    private String userFilesLocation;
    private String loaderFilesLocation;
    private String intermediateFilesLocation;

    public CropConfig(String serviceDomain,
                      Integer servicePort,
                      String userFilesLocation,
                      String loaderFilesLocation,
                      String intermediateFilesLocation) {

        this.serviceDomain = serviceDomain;
        this.servicePort = servicePort;
        this.userFilesLocation = userFilesLocation;
        this.loaderFilesLocation = loaderFilesLocation;
        this.intermediateFilesLocation = intermediateFilesLocation;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getUserFilesLocation() {
        return userFilesLocation;
    }

    public String getLoaderFilesLocation() {
        return loaderFilesLocation;
    }

    public String getIntermediateFilesLocation() {
        return intermediateFilesLocation;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }


}
