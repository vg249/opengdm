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


//    @Pointcut("execution(public * *(..))")
//    //@Pointcut(value="execution(* org.gobiiproject.gobiidtomapping.*.create(dto)), && args(dto)")
//    private void allMapMethods() {}

    //@Before(value="execution(* org.gobiiproject.gobiidtomapping.*.create(dto)), && args(dto)", argNames="dto")

    //@Before("allMapMethods()")
    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.*.create(*))")
    public void beforeCreate(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[0];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setCreatedDate(new Date());
        }
    }

    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.*.replace(*,*))")
    public void beforeReplace(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[1];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setModifiedDate(new Date());
        }
    }
}
