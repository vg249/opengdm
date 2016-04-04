package org.gobiiproject.gobiidao.entityaccess.impl.mock;

import org.gobiiproject.gobiidao.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entities.Marker;
import org.gobiiproject.gobiidao.entityaccess.PingDao;
import org.gobiiproject.gobiimodel.logutils.LineUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/4/2016.
 */
public class PingDaoImplMock extends DaoImplHibernate<Marker> implements PingDao {

    @Override
    public List<String> getPingResponses(List<String> pingRequests) {

        List<String> returnVal = new ArrayList<>();

        for(String currentString : pingRequests ) {

            String responseLine = LineUtils.wrapLine("DAO Layer responds: " + currentString);
            returnVal.add( responseLine);

        } // iterate ping requests

        return (returnVal);

    } // getPingResponses()

} // PingDaoImplMock
