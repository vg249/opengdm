// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;
import org.gobiiproject.gobiiclient.core.restmethods.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;


public class DtoRequestNameIdList {


    public NameIdListDTO process(NameIdListDTO nameIdListDTORequest) throws Exception {

        return new DtoRequestProcessor<NameIdListDTO>().process(nameIdListDTORequest,
                NameIdListDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_NAME_ID_LIST);

    } // getContactsById()

}
