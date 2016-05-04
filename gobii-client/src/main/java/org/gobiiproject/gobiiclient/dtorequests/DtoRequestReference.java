// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.RestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestReference {

    public ReferenceDTO processReference(ReferenceDTO referenceDTO) throws Exception {

        ReferenceDTO returnVal = null;

        RestRequest<ReferenceDTO> restRequest = new RestRequest<>(ReferenceDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponseForDto(Urls.URL_REFERENCE, referenceDTO, token);

        return returnVal;

    } // getPing()

//    public ProjectDTO updateProject(ProjectDTO projectDTO) throws Exception {
//
//        ProjectDTO returnVal = null;
//
//        RestRequest<ProjectDTO> restRequest = new RestRequest<>(ProjectDTO.class);
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
