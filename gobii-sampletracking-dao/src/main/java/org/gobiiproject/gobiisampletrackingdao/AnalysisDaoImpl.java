package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Transactional
public class AnalysisDaoImpl implements AnalysisDao {

    Logger LOGGER = LoggerFactory.getLogger(AnalysisDaoImpl.class);

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

            analyses = em.createQuery(criteriaQuery)
                    .getResultList();

            return analyses;
        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

}
