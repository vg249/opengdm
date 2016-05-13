// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.TypedRestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;

public class DtoRequestDataSet {


    public DataSetDTO process(DataSetDTO DataSetDTO) throws Exception {

        DataSetDTO returnVal = null;

        TypedRestRequest<DataSetDTO> typedRestRequest = new TypedRestRequest<>(DataSetDTO.class);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        returnVal = typedRestRequest.getTypedHtppResponseForDto(Urls.URL_DATASET, DataSetDTO, token);

        return returnVal;

    } // getPing()


}
