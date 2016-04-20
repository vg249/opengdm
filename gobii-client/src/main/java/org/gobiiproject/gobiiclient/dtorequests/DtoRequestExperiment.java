// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.RestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestExperiment {

    public ExperimentDTO getExperiment(ExperimentDTO experimentDTO) throws Exception {

    	  ExperimentDTO returnVal = null;

          RestRequest<ExperimentDTO> restRequest = new RestRequest<>(ExperimentDTO.class);

          SystemUsers systemUsers = new SystemUsers();
          SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
          String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

          returnVal = restRequest.getTypedHtppResponseForDto(Urls.URL_EXPERIMENT, experimentDTO, token);

          return returnVal;

    } // getPing()


} // DtoRequestMarkers()
