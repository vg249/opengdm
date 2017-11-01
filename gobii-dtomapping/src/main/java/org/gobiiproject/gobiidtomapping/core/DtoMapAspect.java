package org.gobiiproject.gobiidtomapping.core;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gobiiproject.gobiidao.cache.TableTrackingCache;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.hibernate.type.DateType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/***
 * The intent of this class is to set the created/modified date and user values for all
 * DTOs that derive from DTOBaseAuditable. All DtoMap* classes for tables that have
 * the created_date, modified_date, created_by, and modified_by columns should
 * derive from DtoMap<> so that the DtoMap* class's method names will conform to
 * a naming (e.g., create() for insert). They must also live in the entity.auditable
 * namespace. The JoinPoint configurations in this class will then always and systematically
 * down-cast the dto to DTOBaseAuditable and set the appropriate methods.
 * In order for this class to be invoked, the application-config.xml file must have the
 *     <aop:aspectj-autoproxy proxy-target-class="true"/>
 * annotation.
 * It would be ideal if there were a way to configure the advice so that
 * it would be invoked only when the method argument was of type DTOBaseAuditable
 * The instanceof idiom is not great OO design. However, getting this piece to work
 * at all was a challenge. Configuring the join point to trigger in this way just
 * did not want to work. The next best thing is, as we have done here, to make
 * all DtoMap* classes for the appropriate tables derive from DTOBaseAuditable and live in a
 * namespace such that the JoinPoint configuration can target the names of the methods.
 */
@Aspect
public class DtoMapAspect {


    @Autowired
    private TableTrackingCache tableTrackingCache;


    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.create(*))")
    public void beforeCreate(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[0];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            Date createDate = new Date();
            dto.setCreatedDate(createDate);

            //use the cache until the date columns in the database support the time portion of the date.
            this.tableTrackingCache.setLastModified(dto.getEntityNameType(), createDate);
        }
    }

    @After(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.create(*))")
    public void afterCreate(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[0];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            this.tableTrackingCache.setCount(dto.getEntityNameType());
        }
    }

    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.replace(*,*))")
    public void beforeReplace(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[1];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            Date modifiedDate = new Date();
            dto.setModifiedDate(modifiedDate);

            //use the cache until the date columns in the database support the time portion of the date.
            this.tableTrackingCache.setLastModified(dto.getEntityNameType(), modifiedDate);
        }
    }
}
