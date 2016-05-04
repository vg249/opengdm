// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.RestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestMapset {


    public MapsetDTO processMapset(MapsetDTO MapsetDTO) throws Exception {

        MapsetDTO returnVal = null;

        RestRequest<MapsetDTO> restRequest = new RestRequest<>(MapsetDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = restRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = restRequest.getTypedHtppResponseForDto(Urls.URL_MAPSET, MapsetDTO, token);

        return returnVal;

    } // getPing()



}
