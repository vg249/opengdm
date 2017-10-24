package org.gobiiproject.gobiidtomapping.core;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.hibernate.type.DateType;

import java.util.Date;

@Aspect
public class DtoMapAspect {


    /***
     * It would be ideal if there were a way to configure the advice so that
     * it would be invoked only when the method argument was of type DTOBaseAuditable
     * The instanceof idiom is not great OO design. However, getting this piece to work
     * at all was a challenge. Configuring the join point to trigger in this way just
     * did not want to work. The mapping classes are being set up now t inherit from a
     * base class that enforceds the create() and replace() naming convention -- that at
     * least should ensure the specificity of the target
     */
    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.create(*))")
    public void beforeCreate(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[0];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setCreatedDate(new Date());
        }
    }

    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.replace(*,*))")
    public void beforeReplace(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[1];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setModifiedDate(new Date());
        }
    }
}
