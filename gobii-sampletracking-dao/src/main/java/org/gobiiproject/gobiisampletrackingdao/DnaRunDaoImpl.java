package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
public class DnaRunDaoImpl implements DnaRunDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaRunDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    /**
     * Lists the DnaRun Entities which matches the given criterias.
     * @param pageSize - Number of rows to be fetched
     * @param rowOffset - Number of rows to be offseted before fetching tuples
     * @param dnaRunId - Dna Run Id which uniquely identifies dna run
     * @param datasetId - Dataset Id to which dnaruns belong to.
     * @param dnaSampleId - dnaSampleId filter. to fetch only dnaruns for given dnasample Id.
     * @param dnaSampleName - dnaSample Id filter. to fetch only dnaruns for given dnasample Name.
     * @return
     */
    @Override
    public List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                                   Integer dnaRunId, String dnaRunName,
                                   Integer datasetId, Integer experimentId,
                                   Integer dnaSampleId, String dnaSampleName,
                                   Integer germplasmId, String germplasmName) {


        Objects.requireNonNull(pageSize, "Page size should not be null");
        Objects.requireNonNull(rowOffset, "Row Offset should not be null");

        List<DnaRun> dnaRuns = new ArrayList<>();

        List<Predicate> predicates = new ArrayList<>();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<DnaRun> criteriaQuery = criteriaBuilder.createQuery(DnaRun.class);

            Root<DnaRun> dnaRunRoot = criteriaQuery.from(DnaRun.class);
            criteriaQuery.select(dnaRunRoot);

            Join<Object, Object> dnaSampleJoin = (
                    (Join<Object, Object>) dnaRunRoot.fetch("dnaSample"));

            Join<Object, Object> germplasmJoin = (
                    (Join<Object, Object>) dnaSampleJoin.fetch("germplasm"));

            Join<Object, Object> experimentJoin = (
                    (Join<Object, Object>) dnaRunRoot.fetch("experiment"));


            if(dnaSampleId  != null || dnaSampleName != null
                    || germplasmId != null || germplasmName != null
            ) {

                if(dnaSampleId != null) {
                    predicates.add(criteriaBuilder.equal(
                            dnaSampleJoin.get("dnaSampleId"),
                            dnaSampleId));
                }

                if(dnaSampleName != null) {
                    predicates.add(criteriaBuilder.equal(
                            dnaSampleJoin.get("dnaSampleName"),
                            dnaSampleName));
                }

                if(germplasmId != null || germplasmName != null) {

                    if(germplasmId != null) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        germplasmJoin.get("germplasmId"),
                                        germplasmId));
                    }

                    if(germplasmName != null) {
                        predicates.add(criteriaBuilder.equal(
                                germplasmJoin.get("germplasmName"),
                                germplasmName));
                    }

                }

            }

            if(experimentId != null) {
                predicates.add(
                        criteriaBuilder.equal(experimentJoin.get("experiemntId"), experimentId));

            }

            if(datasetId != null) {
                Expression<Boolean> datasetIdExists = criteriaBuilder.function(
                        "JSONB_EXISTS", Boolean.class,
                        dnaRunRoot.get("datasetDnaRunIdx"), criteriaBuilder.literal(datasetId.toString()));

                predicates.add(criteriaBuilder.isTrue(datasetIdExists));
            }

            if(dnaRunId != null) {
                predicates.add(criteriaBuilder.equal(dnaRunRoot.get("dnaRunId"), dnaRunId));
            }

            if(dnaRunName != null) {
                predicates.add(criteriaBuilder.equal(dnaRunRoot.get("dnaRunName"), dnaRunName));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            dnaRuns = em.createQuery(criteriaQuery)
                    .setFirstResult(rowOffset)
                    .setMaxResults(pageSize)
                    .getResultList();

            return dnaRuns;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Override
    public List<DnaRun> getDnaRunsByDnaRunIdCursor(
            Integer dnaRunIdCursor,
            Integer datasetId,
            Integer pageSize) {

        List<DnaRun> dnaRuns;

        Objects.requireNonNull(pageSize, "Page Size is required");

        String queryString = "SELECT {dnarun.*} " +
                " FROM dnarun AS dnarun " +
                " WHERE (dnarun.dataset_dnarun_idx->CAST(:datasetId AS TEXT) IS NOT NULL OR :datasetId IS NULL) " +
                " AND (:dnaRunIdCursor IS NULL OR dnarun.dnarun_id > :dnaRunIdCursor) " +
                " ORDER BY dnarun.dnarun_id " +
                " LIMIT :pageSize ";

        try {


            Session session = em.unwrap(Session.class);

            dnaRuns = session.createNativeQuery(queryString)
                    .addEntity("dnarun", DnaRun.class)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .setParameter("dnaRunIdCursor", dnaRunIdCursor, IntegerType.INSTANCE)
                    .list();

            return dnaRuns;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }

    /**
     * Get DnaRun Entity by dnaRun Id.
     * dnaRunId is unique id for dnaRunEntity.
     * @param dnaRunId - dna run id for which DnaRun Entity needs to be fetched.
     * @return - DnaRun Entity with the given id
     */
    @Override
    public DnaRun getDnaRunById(Integer dnaRunId) {

        try {

            List<DnaRun> dnaRunsById = this.getDnaRuns(null, null,
                    dnaRunId, null,
                    null, null,
                    null, null,
                    null, null);

            if (dnaRunsById.size() > 1) {

                LOGGER.error("More than one duplicate entries found.");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "More than one dnarun entity exists for the same Id");

            } else if (dnaRunsById.size() == 0) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dna run Entity for given id does not exist");
            }

            return dnaRunsById.get(0);

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
     * Lists DnaRun entities for given dataset Id
     * @param datasetId - ID of dataset to which dnaruns belong.
     * @param pageSize - Number of rows to be fetched
     * @param rowOffset - Number of rows to offset before fetching
     * @return - List of result DnaRun Entities.
     */
    @Override
    public List<DnaRun> getDnaRunsByDatasetId(Integer datasetId, Integer pageSize,
                                              Integer rowOffset) {

        return getDnaRuns(pageSize, rowOffset,
                null, null,
                datasetId, null,
                null, null,
                null, null);

    }


    /**
     * Lists DnaRun Entitis with given DnaRun Id
     * @param dnaRunIds - List of IDs of dnaRun.
     * @return - List of DnaRun Entity result
     */
    @Override
    public List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds) {

        List<DnaRun> dnaruns = new ArrayList<>();

        try {

            CriteriaBuilder cb  = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<DnaRun> criteria = cb.createQuery(DnaRun.class);

            //Set Root entity and selected entities
            Root<DnaRun> root = criteria.from(DnaRun.class);

            criteria.select(root);


            criteria.where(root.get("dnaRunId").in(dnaRunIds));

            criteria.orderBy(cb.asc(root.get("dnaRunId")));

            dnaruns =  em.createQuery(criteria).getResultList();

            return dnaruns;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }


    /**
     * Lists DnaRun Entities with given DnaRun Names
     * @param dnaRunNames - List of Names for which dnaRuns need to be fecthed.
     * @return - List of DnaRun Entity result.
     */
    @Override
    public List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames) {

        List<DnaRun> dnaruns = new ArrayList<>();

        try {

            CriteriaBuilder cb  = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<DnaRun> criteria = cb.createQuery(DnaRun.class);

            //Set Root entity and selected entities
            Root<DnaRun> root = criteria.from(DnaRun.class);
            criteria.select(root);


            criteria.where(root.get("dnaRunName").in(dnaRunNames));

            criteria.orderBy(cb.asc(root.get("dnaRunName")));

            dnaruns = em.createQuery(criteria).getResultList();

            return dnaruns;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }

    @Override
    public List<DnaRun> getDnaRunsByDatasetIdNoAssociations(Integer datasetId,
                                                            Integer pageSize,
                                                            Integer rowOffset) throws GobiiException {

        List<DnaRun> dnaRuns = new ArrayList<>();

        Objects.requireNonNull(pageSize);
        Objects.requireNonNull(rowOffset);

        String queryString = "SELECT {dnarun.*} FROM dnarun " +
                "WHERE jsonb_exists(dnarun.dataset_dnarun_idx, CAST(:datasetId AS TEXT))";

        try {

            Session session = em.unwrap(Session.class);

            dnaRuns = session
                    .createNativeQuery(queryString)
                    .addEntity("dnarun", DnaRun.class)
                    .setParameter("datasetId", datasetId)
                    .setMaxResults(pageSize)
                    .setFirstResult(rowOffset)
                    .list();

        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
        return dnaRuns;
    }

}
