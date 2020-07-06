package org.gobiiproject.gobidomain.services.gdmv3.exceptions;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * This exception is for errors where an associated/related object (from the main entity)
 * was not found.  This is intended for HTTP 400 response in web handler and should be
 * differentiated from EntityDoesNotExistException.
 */
@SuppressWarnings("serial")
public class UnknownEntityException extends GobiiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnknownEntityException(String entityType) {
        super(
            GobiiStatusLevel.ERROR,
            GobiiValidationStatusType.BAD_REQUEST,
            String.format("Unknown %s", entityType)
        );
    }

    public static class Reference extends UnknownEntityException {
        public Reference() { super("Reference"); }   
    }

    public static class MapsetType extends UnknownEntityException {
        public MapsetType() { super("Mapset type"); }
    }

    public static class Analysis extends UnknownEntityException {
        public Analysis() { super("Analysis"); }
    }

	public static class Contact extends UnknownEntityException {
        public Contact() { super("Contact"); }
	}

	public static class Project extends UnknownEntityException {
        public Project() { super("Project"); }
	}

	public static class VendorProtocol extends UnknownEntityException {
        public VendorProtocol() { super("Vendor Protocol"); }
	}
    
}