package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class RestCallProfileDTO {

    //default ctor needed for serialization
    public RestCallProfileDTO() {}

    public RestCallProfileDTO(RestResourceId restResourceId,
                              List<RestMethodTypes> supportedVerbs,
                              Integer maxPostPut,
                              Integer maxGet) {
        this.restResourceId = restResourceId;
        this.supportedVerbs = supportedVerbs;
        this.maxPostPut = maxPostPut;
        this.maxGet = maxGet;
    }


    @Element(required = false)
    RestResourceId restResourceId;

    @ElementList(required = false)
    List<RestMethodTypes> supportedVerbs = new ArrayList<>();

    @Element(required = false)
    Integer maxPostPut = 100;

    @Element(required = false)
    Integer maxGet = 500;

    public RestResourceId getRestResourceId() {
        return restResourceId;
    }

    public void setRestResourceId(RestResourceId restResourceId) {
        this.restResourceId = restResourceId;
    }

    public List<RestMethodTypes> getSupportedVerbs() {
        return supportedVerbs;
    }

    public void setSupportedVerbs(List<RestMethodTypes> supportedVerbs) {
        this.supportedVerbs = supportedVerbs;
    }

    public Integer getMaxPostPut() {
        return maxPostPut;
    }

    public void setMaxPostPut(Integer maxPostPut) {
        this.maxPostPut = maxPostPut;
    }

    public Integer getMaxGet() {
        return maxGet;
    }

    public void setMaxGet(Integer maxGet) {
        this.maxGet = maxGet;
    }
}
