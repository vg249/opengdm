package org.gobiiproject.gobiidao.entities.access.impl.mock;

import org.gobiiproject.gobiidao.entities.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entities.pojos.Marker;
import org.gobiiproject.gobiidao.entities.access.PingDao;
import org.gobiiproject.gobiimodel.logutils.LineUtils;

import java.util.ArrayList;
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
