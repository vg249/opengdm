package org.gobiiproject.gobidomain.services.gdmv3.exceptions;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

public class EntityNotFoundException extends GobiiDaoException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entityType) {
        super(
            GobiiStatusLevel.ERROR,
            GobiiValidationStatusType.BAD_REQUEST,
            String.format("Unknown %s", entityType)
        );
    }
    
}