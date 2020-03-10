package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.gobiiproject.gobiimodel.entity.v3.GobiiProject;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;

public class ProjectDaoImpl implements ProjectDao {

    Logger LOGGER = LoggerFactory.getLogger(ProjectDaoImpl.class);
    
    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;
    
    @Transactional
    @Override
    public List<GobiiProject> getProjects(Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        LOGGER.debug("DAO getting projects");
        List<GobiiProject> projects = new ArrayList<>();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<GobiiProject> criteriaQuery = criteriaBuilder.createQuery(GobiiProject.class);

            Root<GobiiProject> projectRoot = criteriaQuery.from(GobiiProject.class);
            criteriaQuery.select(projectRoot);
            projects = em.createQuery(criteriaQuery)
                .setFirstResult(pageNum * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
            return projects;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.UNKNOWN,
                e.getMessage() +  " Cause Message: " + e.getCause().getMessage()
             );
        }
        
    }

    
}