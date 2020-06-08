package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.gobiiproject.gobiimodel.entity.MarkerGroup;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarkerGroupDaoImpl implements MarkerGroupDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public MarkerGroup createMarkerGroup(MarkerGroup markerGroup) {
        em.persist(markerGroup);
        em.flush();
        return markerGroup;
    }


	@Override
	public List<MarkerGroup> getMarkerGroups(Integer offset, Integer pageSize) {
        List<MarkerGroup> markerGroups = new ArrayList<>();
  
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<MarkerGroup> criteriaQuery = criteriaBuilder.createQuery(MarkerGroup.class);
  
            Root<MarkerGroup> root = criteriaQuery.from(MarkerGroup.class);
       
            criteriaQuery.select(root);
  
            markerGroups = em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(pageSize).getResultList();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
  
          throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                  e.getMessage() + " Cause Message: " + e.getCause().getMessage());
  
        }
        return markerGroups;  
        
		
	}


	@Override
	public MarkerGroup getMarkerGroup(Integer markerGroupId) {
		return em.find(MarkerGroup.class, markerGroupId);
	}
    
}