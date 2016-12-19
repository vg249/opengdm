package org.gobiiproject.gobiiclient.core.restmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelope;
import org.gobiiproject.gobiiclient.core.HttpMethodResult;

import java.util.List;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiResourceDerived<T_POST_OBJ_TYPE, T_RESPONSE_TYPE_MASTER,T_RESPONSE_TYPE_DETAIL> {

    private RestUri restUri;
    private ObjectMapper objectMapper = new ObjectMapper();
    private RestResourceUtils restResourceUtils;

    private Class<T_POST_OBJ_TYPE> brapiPostObjType;
    private Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster;
    private Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail;

    public BrapiResourceDerived(RestUri restUri,
                                Class<T_POST_OBJ_TYPE> brapiPostObjType,
                                Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster,
                                Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail) {

        this.restUri = restUri;
        this.brapiPostObjType = brapiPostObjType;
        this.brapiResponseTypeMaster = brapiResponseTypeMaster;
        this.brapiResponseTypeDetail = brapiResponseTypeDetail;
        this.restResourceUtils = new RestResourceUtils();
    }

    private BrapiResponseEnvelope<T_RESPONSE_TYPE_MASTER,T_RESPONSE_TYPE_DETAIL> getTypedListObjFromResult(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseEnvelope<T_RESPONSE_TYPE_MASTER,T_RESPONSE_TYPE_DETAIL> returnVal;
        String responseAsString = httpMethodResult.getPayLoad().toString();
        returnVal = objectMapper.readValue(responseAsString,BrapiResponseEnvelope.class);

        return returnVal;
    }

//    public T_RESPONSE_TYPE_MASTER get() throws Exception {
//
//        T_RESPONSE_TYPE_MASTER returnVal;
//
//        HttpMethodResult httpMethodResult =
//                this.restResourceUtils.getHttp()
//                        .get(this.restUri,
//                                restResourceUtils.getClientContext().getUserToken());
//
//        returnVal = this.getTypedListObjFromResult(httpMethodResult);
//
//        return returnVal;
//    }
//
//    public T_RESPONSE_TYPE_MASTER post(T_POST_OBJ_TYPE bodyObj) throws Exception {
//
//
//        T_RESPONSE_TYPE_MASTER returnVal;
//
//        String bodyAsString = objectMapper.writeValueAsString(bodyObj);
//
//        HttpMethodResult httpMethodResult =
//                this.restResourceUtils.getHttp()
//                        .post(this.restUri,
//                                bodyAsString,
//                                restResourceUtils.getClientContext().getUserToken());
//
//
//        returnVal = this.getTypedListObjFromResult(httpMethodResult);
//
//        return returnVal;
//
//    } //

    public BrapiResponseEnvelope<T_RESPONSE_TYPE_MASTER,T_RESPONSE_TYPE_DETAIL> postToListResource(T_POST_OBJ_TYPE bodyObj) throws Exception {


        BrapiResponseEnvelope<T_RESPONSE_TYPE_MASTER,T_RESPONSE_TYPE_DETAIL> returnVal;

        String bodyAsString = objectMapper.writeValueAsString(bodyObj);

        HttpMethodResult httpMethodResult =
                this.restResourceUtils.getHttp()
                        .post(this.restUri,
                                bodyAsString,
                                restResourceUtils.getClientContext().getUserToken());


        returnVal = this.getTypedListObjFromResult(httpMethodResult);

        return returnVal;

    } //

}
