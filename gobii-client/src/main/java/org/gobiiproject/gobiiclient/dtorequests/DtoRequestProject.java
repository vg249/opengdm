// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.TypedRestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestProject {


    private final String JSON_PROP_INVESTIGATORS = "principleInvestigators";
    private final String JSON_PROP_PROJECTID = "projectId";


    public ProjectDTO getProject(Integer projectId) throws Exception {

        ProjectDTO returnVal = null;

        JsonObject projectRequestJson = new JsonObject();
        projectRequestJson.add(JSON_PROP_INVESTIGATORS, new JsonObject());
        projectRequestJson.addProperty(JSON_PROP_PROJECTID,projectId);

        TypedRestRequest<ProjectDTO> typedRestRequest = new TypedRestRequest<>(ProjectDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = typedRestRequest.getTypedHtppResponse(Urls.URL_PING_PROJECT, projectRequestJson, token);

        return returnVal;

    } // getPing()

    public ProjectDTO process(ProjectDTO projectDTO) throws Exception {

        ProjectDTO returnVal = null;

        TypedRestRequest<ProjectDTO> typedRestRequest = new TypedRestRequest<>(ProjectDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = typedRestRequest.getTypedHtppResponseForDto(Urls.URL_PING_PROJECT, projectDTO, token);

        return returnVal;

    }


}
