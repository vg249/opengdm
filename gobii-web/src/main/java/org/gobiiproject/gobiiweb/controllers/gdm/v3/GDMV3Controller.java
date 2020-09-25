package org.gobiiproject.gobiiweb.controllers.gdm.v3;

public class GDMV3Controller {
    
    public Integer getPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) return 1000;
        return pageSize;
    }
}
