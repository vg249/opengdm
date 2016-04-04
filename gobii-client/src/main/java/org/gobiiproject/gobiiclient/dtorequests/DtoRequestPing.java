// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.RestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

import java.util.List;

public class DtoRequestPing {


    private final String JSON_PROP_PINGREQUESTS = "pingRequests";


    public PingDTO getPing(List<String> requestStrings) throws Exception {

        PingDTO returnVal = null;


        JsonArray requestArray = new JsonArray();

        for (String currentString : requestStrings) {
            requestArray.add(currentString);
        }

        JsonObject pingRequestJson = new JsonObject();
        pingRequestJson.add(JSON_PROP_PINGREQUESTS, requestArray);


        RestRequest<PingDTO> restRequest = new RestRequest<>(PingDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponse(Urls.URL_PING, pingRequestJson, token);

        return returnVal;

    } // getPing()

} // DtoRequestMarkers()
