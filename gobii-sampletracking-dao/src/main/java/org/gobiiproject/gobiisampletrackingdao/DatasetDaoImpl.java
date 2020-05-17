package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data access object Implementation for dataset Entity in the database
 *
 */
public class DatasetDaoImpl implements DatasetDao {

    Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Dataset createDataset(Dataset datasetToCreate)
        throws GobiiDaoException {

        try {
            em.persist(datasetToCreate);
            em.flush();
            return datasetToCreate;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.UNKNOWN,
                e.getMessage() + " Cause Message: "
                    + e.getCause().getMessage());
        }
    }

    /**
     * @param pageSize - size of the page
     * @param rowOffset - Row offset after which the pages need to be fetched
     * @param datasetId - Id for dataset. Unique identifier.
     * @return List of DatsetEntity
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Dataset> getDatasets(Integer pageSize, Integer rowOffset,
                                     Integer datasetId, String datasetName,
                                     Integer experimentId, String experimentName) {

        List<Dataset> datasets;

        List<Predicate> predicates = new ArrayList<>();


        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");
            Objects.requireNonNull(pageSize, "rowOffset : Required non null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> datasetRoot = criteriaQuery.from(Dataset.class);
            criteriaQuery.select(datasetRoot);

            Join<Object, Object> experiment =
                    (Join<Object, Object>) datasetRoot.fetch("experiment");

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

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

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
    public Dataset getDatasetById(Integer datasetId) throws GobiiException {

        Objects.requireNonNull(datasetId, "datasetId : Required non null");

        try {

            //Overload the getDatasets
            List<Dataset> datasetsById = this.getDatasets(1000, 0,
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

    /**
     * Returns a list of object tuple with Dataset entity left joined with analysis entities
     * and their respective marker and dnarun count.
     *
     * @param pageSize - number of dataset entities to be fetched
     * @param rowOffset - row offset for database list
     * @param datasetId - filter by dataset id
     * @param datasetName - filter by dataset name
     * @param experimentId - filter by experiment id
     * @param experimentName - filter by experiment name
     * @return List<Object[]> list of object tuple with,
     * Object[0] - Dataset Entity
     * Object[1] - Analysis Entity related to database. Joined using analyses column
     * Object[2] - markers count for each database entity
     * Object[3] - dnaruns count for each database entity
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getDatasetsWithAnalysesAndCounts(
          Integer pageSize, Integer rowOffset,
          Integer datasetId, String datasetName,
          Integer experimentId, String experimentName) {


        String queryString = "WITH ds AS (" +
                "SELECT * " +
                "FROM dataset " +
                "WHERE (:datasetId IS NULL OR dataset_id = :datasetId) " +
                "AND (:datasetName IS NULL OR dataset.name = :datasetName) " +
                "LIMIT :pageSize OFFSET :rowOffset) " +
                "SELECT {ds.*} , {anas.*}, {experiment.*}, {callinganalysis.*}, {job.*}, " +
                "{typeCv.*}, {statusCv.*}, " +
                "(SELECT gettotalmarkersindataset " +
                "FROM gettotalmarkersindataset(CAST(ds.dataset_id AS TEXT))) " +
                "AS marker_count, " +
                "(SELECT gettotaldnarunsindataset " +
                "FROM gettotaldnarunsindataset(CAST(ds.dataset_id AS TEXT))) " +
                "AS dnarun_count " +
                "FROM ds " +
                "INNER JOIN experiment AS experiment ON(" +
                "   (ds.experiment_id = experiment.experiment_id) " +
                "   AND (:experimentId IS NULL OR experiment.experiment_id = :experimentId) " +
                "   AND (:experimentName IS NULL OR experiment.name = :experimentName) " +
                ") " +
                "LEFT JOIN analysis AS anas ON(anas.analysis_id = ANY(ds.analyses)) " +
                "LEFT JOIN analysis AS callinganalysis ON(callinganalysis.analysis_id = ds.callinganalysis_id) " +
                "LEFT JOIN job USING(job_id) " +
                "LEFT JOIN cv typeCv ON(job.type_id = typeCv.cv_id) " +
                "LEFT JOIN cv statusCv ON(job.status = statusCv.cv_id) ";

        Objects.requireNonNull(pageSize, "pageSize: Required non null");
        Objects.requireNonNull(rowOffset, "rowOffset: Required non null");

        try {

            Session session = em.unwrap(Session.class);

            List<Object[]> resultTupleList = session.createNativeQuery(queryString)
                    .addEntity("ds", Dataset.class)
                    .addEntity("anas", Analysis.class)
                    .addJoin("experiment", "ds.experiment")
                    .addJoin("job", "ds.job")
                    .addJoin("callinganalysis", "ds.callingAnalysis")
                    .addJoin("typeCv", "job.type")
                    .addJoin("statusCv", "job.status")
                    .addScalar("marker_count", IntegerType.INSTANCE)
                    .addScalar("dnarun_count", IntegerType.INSTANCE)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .setParameter("experimentId", experimentId, IntegerType.INSTANCE)
                    .setParameter("datasetName", datasetName, StringType.INSTANCE)
                    .setParameter("experimentName", experimentName, StringType.INSTANCE)
                    .list();

            return resultTupleList;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }

}
