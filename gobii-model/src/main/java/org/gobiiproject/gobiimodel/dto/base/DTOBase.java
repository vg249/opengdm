package org.gobiiproject.gobiimodel.dto.base;

<<<<<<< HEAD
import org.codehaus.jackson.annotate.JsonIgnore;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.Date;
=======
>>>>>>> develop
import java.util.HashSet;
import java.util.Set;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

/**
 * Created by Phil on 9/25/2016.
 */
public abstract class DTOBase {


    private Set<GobiiProcessType> allowedProcessTypes = new HashSet<>();

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public Set<GobiiProcessType> getAllowedProcessTypes() {
        return allowedProcessTypes;
    }

    public void setAllowedProcessTypes(Set<GobiiProcessType> allowedProcessTypes) {
        this.allowedProcessTypes = allowedProcessTypes;
    }

}
