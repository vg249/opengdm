package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisDaoImpl implements AnalysisDao {

    Logger LOGGER = LoggerFactory.getLogger(AnalysisDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Analysis createAnalysis(Analysis analysisToCreate) {

        try {
            em.persist(analysisToCreate);
            em.flush();
            return analysisToCreate;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: "
                            + e.getCause().getMessage());
        }
    }

    @Override
    public List<Analysis> getAnalysesByAnalysisIds(Set<Integer> analysisIds) {

        List<Analysis> analyses;


        try {

            Objects.requireNonNull(
                    analysisIds,
                    "analysisId : Required non null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Analysis> criteriaQuery =
                    criteriaBuilder.createQuery(Analysis.class);

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
                    e.getMessage() + " Cause Message: "
                            + e.getCause().getMessage());
        }

    }

    @Override
    public void deleteAnalysis(Integer analysisId) throws Exception {
        try {
            Analysis analysisToDelete = em.find(Analysis.class, analysisId);
            em.remove(analysisToDelete);
            em.flush();
        } catch (javax.persistence.PersistenceException pe) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.FOREIGN_KEY_VIOLATION,
                    "Associated resources found." +
                            " Cannot complete the action" +
                            " unless they are deleted.");
        } catch (Exception e) {
            throw e;
        }
    }


}
