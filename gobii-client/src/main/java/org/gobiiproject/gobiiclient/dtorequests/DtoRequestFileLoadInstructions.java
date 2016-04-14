// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.RestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestFileLoadInstructions {


    private final String LOADER_INSTRUCTIONS = "loaderInstructions";


    //throw away later:
    private final String JSON_PROP_NAMESBYID = "namesById";
    private final String JSON_PROP_ENTITYNAME = "entityName";
    private final String JSON_PROP_FILTER = "filter";


    public LoaderInstructionFilesDTO getSampleInstructionFile() throws Exception {


        LoaderInstructionFilesDTO returnVal = null;

        JsonObject contactsByRoleJson = new JsonObject();
        contactsByRoleJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        contactsByRoleJson.addProperty(JSON_PROP_ENTITYNAME, "contact");
        contactsByRoleJson.addProperty(JSON_PROP_FILTER, "foo");


        RestRequest<LoaderInstructionFilesDTO> restRequest = new RestRequest<>(LoaderInstructionFilesDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponse(Urls.URL_FILE_LOAD_INSTRUCTIONS, contactsByRoleJson, token);

        return returnVal;

    } //

    public LoaderInstructionFilesDTO sendInstructionFile(LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws Exception {


        LoaderInstructionFilesDTO returnVal = null;

//        JsonObject ContactsByRoleJson = new JsonObject();
//        ContactsByRoleJson.add(JSON_PROP_NAMESBYID, new JsonObject());
//        ContactsByRoleJson.addProperty(JSON_PROP_ENTITYNAME, "contact");
//        ContactsByRoleJson.addProperty(JSON_PROP_FILTER, "foo");


//        JsonObject projectRequestJson = new JsonObject();
//        projectRequestJson.add(LOADER_INSTRUCTIONS, new JsonObject());

        RestRequest<LoaderInstructionFilesDTO> restRequest = new RestRequest<>(LoaderInstructionFilesDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponseForDto(Urls.URL_FILE_LOAD_INSTRUCTIONS, loaderInstructionFilesDTO, token);

        return returnVal;

    } //


} // DtoRequestMarkers()
