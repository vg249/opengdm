package org.gobiiproject.gobiiclient.core.restmethods;

import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 9/7/2016.
 */
public class RestUri {

    public RestUri(String requestTemplate,
                   String pathVarDelimBegin,
                   String pathVarDelimEnd) {
        this.requestTemplate = requestTemplate;
        this.pathVarDelimBegin = pathVarDelimBegin;
        this.pathVarDelimEnd = pathVarDelimEnd;
    } // ctor

    private String pathVarDelimBegin;
    private String pathVarDelimEnd;

    private String requestTemplate;
    private Map<String, ResourceParam> paramMap = new HashMap<>();
    private List<ResourceParam> resourceParams = new ArrayList<>();

    public List<ResourceParam> getRequestParams() {
        return this.resourceParams
                .stream()
                .filter(getParam -> getParam.getResourceParamType().equals(ResourceParam.ResourceParamType.QueryParam))
                .collect(Collectors.toList());
    }

    public void addParam(ResourceParam.ResourceParamType resourceParamType,
                         String name) {

        ResourceParam resourceParam = new ResourceParam(resourceParamType, name, null);
        this.paramMap.put(resourceParam.getName(), resourceParam);
        this.resourceParams.add(resourceParam);

    }

    public void setParamValue(String paramName, String value) throws Exception {

        if (null == this.paramMap.get(paramName)) {
            throw new Exception("Specified parameter does not exist: " + paramName);
        }

        this.paramMap.get(paramName).setValue(value);
    }

    public String makeUrl() throws Exception {

        String returnVal = this.requestTemplate; // in case there are no path variables

        List<ResourceParam> pathParams = resourceParams
                .stream()
                .filter(getRequestParam -> getRequestParam.getResourceParamType()
                        .equals(ResourceParam.ResourceParamType.UriParam))
                .collect(Collectors.toList());

        for (ResourceParam currentParam : pathParams) {
            String paramToReplace = pathVarDelimBegin
                    + currentParam.getName()
                    + pathVarDelimEnd;

            if (this.requestTemplate.contains(paramToReplace)) {

                if (false == LineUtils.isNullOrEmpty(currentParam.getValue())) {

                    returnVal = returnVal.replace(paramToReplace, currentParam.getValue());
                } else {
                    throw new Exception("The path variable parameter "
                            + paramToReplace
                            + " does not have a value");
                }
            } else {
                throw new Exception("The request template "
                        + this.requestTemplate
                        + "does not contain the path path variable "
                        + paramToReplace);
            }
        }

        if (returnVal.contains(this.pathVarDelimBegin)) {
            String missingParameter = returnVal
                    .substring(
                            returnVal.indexOf(this.pathVarDelimBegin),
                            returnVal.indexOf(this.pathVarDelimEnd)
                    );

            throw new Exception("There is no parameter for path variable " + missingParameter);
        }

        return returnVal;

    } // makeUrl

} // class RestUri
