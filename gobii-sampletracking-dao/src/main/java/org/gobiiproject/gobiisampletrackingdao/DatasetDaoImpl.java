package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.QueryField;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Data access object Implementation for dataset Entity in the database
 *
 */
public class DatasetDaoImpl implements DatasetDao {

    Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;


    /**
     * @param pageSize - size of the page
     * @param rowOffset - Row offset after which the pages need to be fetched
     * @param datasetId - Id for dataset. Unique identifier.
     * @return List of DatsetEntity
     */
    @Override
    @Transactional
    public List<Dataset> getDatasets(Integer pageSize, Integer rowOffset,
                                     Integer datasetId, String datasetName,
                                     Integer experimentId, String experimentName) {

        List<Dataset> datasets;

        List<Predicate> predicates = new ArrayList<>();

        Objects.requireNonNull(pageSize, "pageSize : Required non null");
        Objects.requireNonNull(pageSize, "rowOffset : Required non null");

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> datasetRoot = criteriaQuery.from(Dataset.class);

            Join<Object, Object> callingAnalysis = (Join<Object, Object>) datasetRoot.fetch(
                    "callingAnalysis",
                    JoinType.LEFT);

            //Inner join as there is referential integrity between experiment and dataset
            Join<Object, Object> experiment = (Join<Object, Object>) datasetRoot.fetch("experiment");
            Join<Object, Object> job = (Join<Object, Object>) datasetRoot.fetch("job", JoinType.LEFT);

            Join<Object, Object> typeCv = (Join<Object, Object>) datasetRoot.fetch("type", JoinType.LEFT);

            Join<Object, Object> statusCv = (Join<Object, Object>) datasetRoot.fetch("status", JoinType.LEFT);

            if(datasetId != null) {
                predicates.add(criteriaBuilder.equal(datasetRoot.get("datasetId"), datasetId));
            }

            if(datasetName != null) {
                predicates.add(criteriaBuilder.equal(datasetRoot.get("datasetName"), datasetName));
            }

            if(experimentId != null) {
                predicates.add(criteriaBuilder.equal(experiment.get("experimentId"), experimentId));
            }

            if(experimentName != null) {
                predicates.add(criteriaBuilder.equal(experiment.get("experimentName"), experimentName));
            }

            datasets = em.createQuery(criteriaQuery)
                    .setFirstResult(rowOffset)
                    .setMaxResults(pageSize)
                    .getResultList();


            return datasets;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }
    }


    /**
     * Gets the dataset entity by dataset id.
     * @param datasetId - Dataset Id
     * @return
     */
    @Override
    @Transactional
    public Dataset getDatasetById(Integer datasetId) throws GobiiException {

        try {

            List<Dataset> datasetsById = this.getDatasets(0, 1000,
                    datasetId, null,
                    null, null);

            if (datasetsById.size() > 1) {
                LOGGER.error("More than one duplicate entries found.");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "More than one dataset entity exists for the same Id");

            } else if (datasetsById.size() == 0) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dataset Entity for given id does not exist");
            }

            return datasetsById.get(0);
        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }



}
