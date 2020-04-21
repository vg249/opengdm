package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
public class AnalysisDaoImpl implements AnalysisDao {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<Analysis> getAnalysesByAnalysisIds(Set<Integer> analysisIds) {

        List<Analysis> analyses;

        Objects.requireNonNull(analysisIds, "analysisId : Required non null");

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Analysis> criteriaQuery = criteriaBuilder.createQuery(Analysis.class);

            Root<Analysis> analysisRoot = criteriaQuery.from(Analysis.class);

            criteriaQuery.select(analysisRoot);
            criteriaQuery.where(analysisRoot.get("analysisId").in(analysisIds));

            analyses = em.createQuery(criteriaQuery).getResultList();

            return analyses;
        } catch (Exception e) {

            log.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Override
    public List<Analysis> getAnalyses(Integer offset, Integer pageSize) {
        List<Analysis> analyses;

        Objects.requireNonNull(offset, "offset : Required non null");
        Objects.requireNonNull(pageSize, "pageSize : Required non null");

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Analysis> criteriaQuery = criteriaBuilder.createQuery(Analysis.class);

            Root<Analysis> root = criteriaQuery.from(Analysis.class);

            // root.fetch("reference");
            root.join("reference", JoinType.LEFT);
            root.fetch("type");
            root.fetch("status");
            criteriaQuery.select(root);

            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("analysisId")));

            analyses = em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(pageSize).getResultList();

            return analyses;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Override
    public Analysis createAnalysis(Analysis analysis) throws Exception {
        em.persist(analysis);
        em.flush();
        return analysis;
    }
}
