// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.header;

import org.gobiiproject.gobiimodel.dto.header.HeaderAuth;
import org.gobiiproject.gobiimodel.dto.header.HeaderResponse;


import java.io.Serializable;

/**
 * Created by Phil on 3/24/2016.
 */
abstract public class Header implements Serializable {

    public Header() {}

    public Header(ProcessType processType) {
        this.processType = processType;
    }

    public enum ProcessType {CREATE, READ, UPDATE, DELETE}

    private ProcessType processType = ProcessType.READ;
    private HeaderAuth dtoHeaderAuth = new HeaderAuth();
    private HeaderResponse dtoHeaderResponse = new HeaderResponse();
    private HeaderPagination headerPagination = null;



    // we also have String in HeaderAuth; we need it in both cases,
    // because HeaderAuth is returned in the body of an authentication response,
    // and we need that to remain as light weight as possible. Here we need crop type
    // because of the application.
    private String cropType = null;

    // in order for the private properties to be serialized into the JSON,
    // we must use the proper bean naming convention for these getters --
    // this is how the Jackson serializer knows that it should serialize what
    // would other wise be private propeties.
    public HeaderAuth getDtoHeaderAuth() {
        return dtoHeaderAuth;
    }

    public HeaderResponse getDtoHeaderResponse() {
        return dtoHeaderResponse;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }


    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public void setPagination(Integer totalCount,
                              Integer pageSize,
                              Integer totalPages,
                              Integer currentPage) {
        
        this.headerPagination = new HeaderPagination(totalCount,
                pageSize,
                totalPages,
                currentPage);

    } // setPagination()

} // Header
