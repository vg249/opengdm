package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private GobiiControllerType gobiiControllerType;
    private String cropContextRoot;

    public UriFactory(String cropContextRoot, GobiiControllerType gobiiControllerType) {

        this.gobiiControllerType = gobiiControllerType;
        this.cropContextRoot = cropContextRoot;
    }

    public UriFactory(String cropContextRoot) {

        this.gobiiControllerType = GobiiControllerType.GOBII;
        this.cropContextRoot = cropContextRoot;
    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri);
    }

    public RestUri resourceColl(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                gobiiServiceRequestId);

        return returnVal;

    } //

    public static RestUri resourceColl(String contextRoot, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(contextRoot,
                GobiiControllerType.GOBII,
                gobiiServiceRequestId);

        return returnVal;

    } //

    public RestUri resourceByUriIdParam(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                gobiiServiceRequestId)
                .addUriParam(paramName);
    } //

    public RestUri resourceByUriIdParamName(String paramName, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        return new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                gobiiServiceRequestId)
                .addUriParam(paramName);
    } //

    public static RestUri resourceByUriIdParam(String contextRoot, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(contextRoot,
                GobiiControllerType.GOBII,
                gobiiServiceRequestId)
                .addUriParam(paramName);
    } //


    public RestUri childResourceByUriIdParam(GobiiServiceRequestId parentGobiiServiceRequestId, GobiiServiceRequestId childGobiiServiceRequestId) throws Exception {

        String paramName = "id";
        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                parentGobiiServiceRequestId)
                .addUriParam(paramName)
                .appendSegment(childGobiiServiceRequestId);

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                GobiiServiceRequestId.URL_CONTACT_SEARCH)
                .addQueryParam("email")
                .addQueryParam("lastName")
                .addQueryParam("firstName")
                .addQueryParam("userName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                GobiiServiceRequestId.URL_MARKER_SEARCH)
                .addQueryParam("name");

        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                GobiiServiceRequestId.URL_NAMES)
                .addUriParam("entity")
                .addQueryParam("filterType")
                .addQueryParam("filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType,
                GobiiServiceRequestId.URL_FILE_LOAD)
                .addUriParam("directoryName")
                .addQueryParam("fileFormat");

        return returnVal;

    } //
}


