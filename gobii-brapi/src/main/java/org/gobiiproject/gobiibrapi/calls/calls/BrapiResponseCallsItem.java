package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiibrapi.types.BrapiDataTypes;
import org.gobiiproject.gobiimodel.types.RestMethodType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseCallsItem {


    public BrapiResponseCallsItem() {}

    public BrapiResponseCallsItem(RestUri restUri, List<RestMethodType> methods, List<BrapiDataTypes> dataTypes) throws Exception {
        this.call = restUri.getResource();
        this.methods = methods;
        this.datatypes = dataTypes;
    }

    private String call;
    private List<RestMethodType> methods = new ArrayList<>();
    private List<BrapiDataTypes> datatypes = new ArrayList<>();


    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public List<RestMethodType> getMethods() {
        return methods;
    }

    public void setMethods(List<RestMethodType> methods) {
        this.methods = methods;
    }

    public List<BrapiDataTypes> getDatatypes() {
        return this.datatypes;
    }

    public void setDatatypes(List<BrapiDataTypes> datatypes) {
        this.datatypes = datatypes;
    }
}
