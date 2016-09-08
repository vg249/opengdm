package org.gobiiproject.gobiiclient.core.restmethods.get;

/**
 * Created by Phil on 9/7/2016.
 */
public class GetParam {

    public enum ParamType {Unknown, PathVariable,RequestParam}

    public GetParam(ParamType paramType,
                    String name,
                    String value) {

        this.paramType = paramType;
        this.name = name;
        this.value = value;

    } // ctor

    private ParamType paramType = ParamType.Unknown;
    private String name;
    private String value;


    public ParamType getParamType() {
        return paramType;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
