package org.gobiiproject.gobiiapimodel.hateos;

import org.gobiiproject.gobiimodel.types.RestMethodType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Phil on 9/25/2016.
 */
public class Link {

    public Link() {}

    public Link(String href,
                String description) {
        this.href = href;
        this.description = description;
    }

    private String href;
    private String description;
    private Set<RestMethodType> methods = new HashSet<>();

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RestMethodType> getMethods() {
        return methods;
    }

    public void setMethods(Set<RestMethodType> methods) {
        this.methods = methods;
    }
}
