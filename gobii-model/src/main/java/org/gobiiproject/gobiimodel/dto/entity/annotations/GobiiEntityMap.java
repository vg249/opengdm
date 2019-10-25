package org.gobiiproject.gobiimodel.dto.entity.annotations;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.lang.annotation.*;

/**
 * Annotation to Map DTO Object to Database Entity or CV term.
 * If Entity Class is given, CvGroup and Cv type will be ignored.
 * Cv type default to system type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GobiiEntityMap {

    String paramName();

    Class entity() default void.class;

    // Should be ignored if Entity is defined.
    CvGroup cvGroup() default CvGroup.CVGROUP_STATUS;

    // Should be ignored if cvGroup not provided
    GobiiCvGroupType cvType() default GobiiCvGroupType.GROUP_TYPE_SYSTEM;

}
