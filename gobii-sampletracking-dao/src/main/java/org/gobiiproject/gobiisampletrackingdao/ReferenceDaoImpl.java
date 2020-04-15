package org.gobiiproject.gobiisampletrackingdao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gobiiproject.gobiimodel.entity.Reference;

public class ReferenceDaoImpl implements ReferenceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Reference getReference(Integer id) {
        return em.find(Reference.class, id);
    }

    

}