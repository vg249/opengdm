// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.TypedRestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestExperiment {

    public ExperimentDTO process(ExperimentDTO experimentDTO) throws Exception {

    	  ExperimentDTO returnVal = null;

          TypedRestRequest<ExperimentDTO> typedRestRequest = new TypedRestRequest<>(ExperimentDTO.class);

          SystemUsers systemUsers = new SystemUsers();
          SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
          String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

          returnVal = typedRestRequest.getTypedHtppResponseForDto(Urls.URL_EXPERIMENT, experimentDTO, token);

          return returnVal;

    } // getPing()


}
