// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************

package org.gobiiproject.gobiidao.entityaccess.impl.db;


import org.gobiiproject.gobiidao.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entityaccess.MarkerDao;
import org.gobiiproject.gobiidao.generated.entities.Marker;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/24/2016.
 */
public class MarkerDaoImplHibernate extends DaoImplHibernate<Marker> implements MarkerDao  {

    @Override
    public Map<String, List<String>> getMarkers(List<String> chromosomes) {

        throw new NotImplementedException();
    }


}
