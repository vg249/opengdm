// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.RestRequest;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

import java.util.List;

public class DtoRequestMarkers {


    private final String JSON_PROP_NAME = "name";
    private final String JSON_PROP_SCOPE = "scope";

    public MarkerGroupDTO getmMarkerGroup(List<String> chromosomes) throws Exception {

        MarkerGroupDTO returnVal = null;

        SystemUsers systemUsers = new SystemUsers();

        // this request is just for testing only -- it has nothing to do with the real service
        JsonObject markerRequestJson = new JsonObject();
        markerRequestJson.addProperty(JSON_PROP_NAME, "test_val");
        markerRequestJson.addProperty(JSON_PROP_NAME, "test_val");

        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

        RestRequest<MarkerGroupDTO> restRequest = new RestRequest<>(MarkerGroupDTO.class);
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponse(Urls.URL_MARKERS, markerRequestJson, token);

        return returnVal;

    } // getmMarkerGroup()

} // DtoRequestMarkers()
