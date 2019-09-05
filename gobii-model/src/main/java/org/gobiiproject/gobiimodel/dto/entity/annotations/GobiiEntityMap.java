package org.gobiiproject.gobiimodel.dto.entity.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GobiiEntityMap {

    String paramName();

    Class entity();

}
