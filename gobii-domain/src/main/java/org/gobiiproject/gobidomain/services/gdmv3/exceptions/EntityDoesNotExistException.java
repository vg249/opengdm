package org.gobiiproject.gobidomain.services.gdmv3.exceptions;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * EntityDoesNotExistException is intended for exceptions that will cause an HTTP 404
 * Response on the web handler
 */
@SuppressWarnings("serial")
public class EntityDoesNotExistException extends GobiiException {

    /**
     *
     */
    private static final long serialVersionUID = 6549768789435773693L;

    public EntityDoesNotExistException(String entityType) {
        super(GobiiStatusLevel.ERROR,
              GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST, 
              String.format("%s not found", entityType)
        );
    }

    //subclasses

    public static class Mapset extends EntityDoesNotExistException {
        public Mapset() { super("Mapset"); }
    }
}