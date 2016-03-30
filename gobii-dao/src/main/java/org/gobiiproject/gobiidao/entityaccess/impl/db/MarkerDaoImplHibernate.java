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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/24/2016.
 */
public class MarkerDaoImplHibernate extends DaoImplHibernate<Marker> implements MarkerDao  {

    @Override
    public Map<String, List<String>> getMarkers(List<String> chromosomes) {

        Map<String,List<String>> returnVal = new HashMap<>();

        List<String> arrayList = new ArrayList<>();
        arrayList.add("marker1");
        arrayList.add("marker2");

        returnVal.put("Group 1", arrayList);
        returnVal.put("Group 2", arrayList);

        return returnVal;
    }

}
