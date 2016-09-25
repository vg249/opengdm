package org.gobiiproject.gobiiapimodel.payload;

import org.gobiiproject.gobiiapimodel.hateos.Links;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class Payload<T> {

    Links links = new Links();
    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
