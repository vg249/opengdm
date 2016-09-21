package org.gobiiproject.gobiiclient.core.restmethods.get;

import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 9/7/2016.
 */
public class RestUrl {

    public RestUrl(String requestTemplate,
                   String pathVarDelimBegin,
                   String pathVarDelimEnd) {
        this.requestTemplate = requestTemplate;
        this.pathVarDelimBegin = pathVarDelimBegin;
        this.pathVarDelimEnd = pathVarDelimEnd;
    } // ctor

    private String pathVarDelimBegin;
    private String pathVarDelimEnd;

    private String requestTemplate;
    private Map<String, GetParam> paramMap = new HashMap<>();
    private List<GetParam> getParams = new ArrayList<>();

    public List<GetParam> getRequestParams() {
        return this.getParams
                .stream()
                .filter(getParam -> getParam.getParamType().equals(GetParam.ParamType.RequestParam))
                .collect(Collectors.toList());
    }

    public void addParam(GetParam.ParamType paramType,
                         String name) {

        GetParam getParam = new GetParam(paramType, name, null);
        this.paramMap.put(getParam.getName(), getParam);
        this.getParams.add(getParam);

    }

    public void setParamValue(String paramName, String value) throws Exception {

        if (null == this.paramMap.get(paramName)) {
            throw new Exception("Specified parameter does not exist: " + paramName);
        }

        this.paramMap.get(paramName).setValue(value);
    }

    public String makeUrl() throws Exception {

        String returnVal = this.requestTemplate; // in case there are no path variables

        List<GetParam> pathParams = getParams
                .stream()
                .filter(getRequestParam -> getRequestParam.getParamType()
                        .equals(GetParam.ParamType.PathVariable))
                .collect(Collectors.toList());

        for (GetParam currentParam : pathParams) {
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

} // class RestUrl
