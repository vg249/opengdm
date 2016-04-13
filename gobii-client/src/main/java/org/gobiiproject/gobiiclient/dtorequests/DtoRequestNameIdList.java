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
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestNameIdList {


    private final String JSON_PROP_NAMESBYID = "namesById";
    private final String JSON_PROP_ENTITYNAME = "entityName";
    private final String JSON_PROP_FILTER = "filter";


    public NameIdListDTO getContactsById( String Type ) throws Exception {

        NameIdListDTO returnVal = null;


        JsonObject ContactsByRoleJson = new JsonObject();
        ContactsByRoleJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        ContactsByRoleJson.addProperty(JSON_PROP_ENTITYNAME,"contact");
        ContactsByRoleJson.addProperty(JSON_PROP_FILTER,Type);

        RestRequest<NameIdListDTO> restRequest = new RestRequest<>(NameIdListDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponse(Urls.URL_NAME_ID_LIST, ContactsByRoleJson, token);

        return returnVal;

    } // getContactsById()


    public NameIdListDTO getProjectNamesByContact( Integer contactId) throws Exception {

        NameIdListDTO returnVal = null;


        JsonObject projectNamesByContactJson = new JsonObject();
        projectNamesByContactJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        projectNamesByContactJson.addProperty(JSON_PROP_ENTITYNAME,"project");
        projectNamesByContactJson.addProperty(JSON_PROP_FILTER,contactId.toString());

        RestRequest<NameIdListDTO> restRequest = new RestRequest<>(NameIdListDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponse(Urls.URL_NAME_ID_LIST, projectNamesByContactJson, token);

        return returnVal;

    } // getContactsById()





} // DtoRequestMarkers()
