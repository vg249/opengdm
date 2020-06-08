package org.gobiiproject.gobiisampletrackingdao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gobiiproject.gobiimodel.entity.MarkerGroup;

public class MarkerGroupDaoImpl implements MarkerGroupDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public MarkerGroup createMarkerGroup(MarkerGroup markerGroup) {
        em.persist(markerGroup);
        em.flush();
        return markerGroup;
    }
    
}