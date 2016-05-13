// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.TypedRestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestContact {

    public ContactDTO processContact(ContactDTO contactDTO) throws Exception {

        ContactDTO returnVal = null;

        TypedRestRequest<ContactDTO> typedRestRequest = new TypedRestRequest<>(ContactDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = typedRestRequest.getTypedHtppResponseForDto(Urls.URL_CONTACT, contactDTO, token);

        return returnVal;

    } // getPing()

//    public ProjectDTO updateProject(ProjectDTO projectDTO) throws Exception {
//
//        ProjectDTO returnVal = null;
//
//        TypedRestRequest<ProjectDTO> restRequest = new TypedRestRequest<>(ProjectDTO.class);
//
//        SystemUsers systemUsers = new SystemUsers();
//        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());
//
//        returnVal = restRequest.getTypedHtppResponseForDto(Urls.URL_PING_PROJECT, projectDTO, token);
//
//        return returnVal;
//
//    }


}
