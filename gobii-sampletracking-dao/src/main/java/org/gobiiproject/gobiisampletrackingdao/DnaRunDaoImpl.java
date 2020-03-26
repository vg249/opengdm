package org.gobiiproject.gobiisampletrackingdao;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
                                   Integer germplasmId, String germplasmName,
                                   boolean fetchAssociations) {


        Objects.requireNonNull(pageSize, "Page size should not be null");
        Objects.requireNonNull(rowOffset, "Row Offset should not be null");

        List<DnaRun> dnaRuns = new ArrayList<>();

        List<Predicate> predicates = new ArrayList<>();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<DnaRun> criteriaQuery = criteriaBuilder.createQuery(DnaRun.class);

            Root<DnaRun> dnaRunRoot = criteriaQuery.from(DnaRun.class);
            criteriaQuery.select(dnaRunRoot);

            Join<Object, Object> dnaSampleJoin = null;
            Join<Object, Object> germplasmJoin = null;
            Join<Object, Object> experimentJoin = null;

            if(fetchAssociations) {
                dnaSampleJoin= (Join<Object, Object>) dnaRunRoot.fetch("dnaSample");
                germplasmJoin = (Join<Object, Object>) dnaSampleJoin.fetch("germplasm");
                experimentJoin = (Join<Object, Object>) dnaRunRoot.fetch("experiment");
            }
            else {
                if (dnaSampleId != null || dnaSampleName != null
                        || germplasmId != null || germplasmName != null) {
                    dnaSampleJoin = dnaRunRoot.join("dnaSample");

                    if (germplasmId != null || germplasmName != null) {
                        germplasmJoin = dnaSampleJoin.join("germplasm");
                    }
                }
                if(experimentId != null) {
                    experimentJoin = dnaRunRoot.join("experiment");
                }
            }

            if(dnaSampleId != null && dnaSampleJoin != null) {
                predicates.add(criteriaBuilder.equal(
                        dnaSampleJoin.get("dnaSampleId"),
                        dnaSampleId));
            }

            if(dnaSampleName != null && dnaSampleJoin != null) {
                predicates.add(criteriaBuilder.equal(
                        dnaSampleJoin.get("dnaSampleName"),
                        dnaSampleName));
            }


            if(germplasmId != null && germplasmJoin != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                germplasmJoin.get("germplasmId"),
                                germplasmId));
            }

            if(germplasmName != null && germplasmJoin != null) {
                predicates.add(criteriaBuilder.equal(
                        germplasmJoin.get("germplasmName"),
                        germplasmName));
            }

            if(experimentId != null && experimentJoin != null) {
                predicates.add(
                        criteriaBuilder.equal(experimentJoin.get("experimentId"), experimentId));
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
    public List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                                   Integer dnaRunId, String dnaRunName,
                                   Integer datasetId, Integer experimentId,
                                   Integer dnaSampleId, String dnaSampleName,
                                   Integer germplasmId, String germplasmName) {

        return this.getDnaRuns(pageSize, rowOffset,
                dnaRunId, dnaRunName,
                datasetId, experimentId,
                dnaSampleId, dnaSampleName,
                germplasmId, germplasmName,
                true);

    }


    @Override
    public List<DnaRun> getDnaRunsByDnaRunIdCursor(
            Integer pageSize,
            @Nullable  Integer dnaRunIdCursor,
            @Nullable  Integer datasetId) {

        List<DnaRun> dnaRuns;
        List<Predicate> predicates = new ArrayList<>();

        Objects.requireNonNull(pageSize, "Page Size is required");

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<DnaRun> criteriaQuery = criteriaBuilder.createQuery(DnaRun.class);

            Root<DnaRun> dnaRunRoot = criteriaQuery.from(DnaRun.class);

            if(dnaRunIdCursor != null) {
                predicates.add(criteriaBuilder.gt(dnaRunRoot.get("dnaRunId"), dnaRunIdCursor));
            }

            if(datasetId != null) {
                Expression<Boolean> datasetIdExists = criteriaBuilder.function(
                        "JSONB_EXISTS", Boolean.class,
                        dnaRunRoot.get("datasetDnaRunIdx"), criteriaBuilder.literal(datasetId.toString()));

                predicates.add(criteriaBuilder.isTrue(datasetIdExists));
            }

            criteriaQuery.select(dnaRunRoot);
            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            dnaRuns = em.createQuery(criteriaQuery)
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

    /**
     * Get DnaRun Entity by dnaRun Id.
     * dnaRunId is unique id for dnaRunEntity.
     * @param dnaRunId - dna run id for which DnaRun Entity needs to be fetched.
     * @return - DnaRun Entity with the given id
     */
    @Override
    public DnaRun getDnaRunById(Integer dnaRunId, boolean fetchAssociations) {

        try {

            List<DnaRun> dnaRunsById = this.getDnaRuns(2, 0,
                    dnaRunId, null,
                    null, null,
                    null, null,
                    null, null, fetchAssociations);

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
     * Get DnaRun Entity by dnaRun Id.
     * dnaRunId is unique id for dnaRunEntity.
     * @param dnaRunId - dna run id for which DnaRun Entity needs to be fetched.
     * @return - DnaRun Entity with the given id
     */
    @Override
    public DnaRun getDnaRunById(Integer dnaRunId) {
        return this.getDnaRunById(dnaRunId, true);
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
                null, null,
                true);

    }

    /**
     * Lists DnaRun entities for given dataset Id
     * @param datasetId - ID of dataset to which dnaruns belong.
     * @param pageSize - Number of rows to be fetched
     * @param rowOffset - Number of rows to offset before fetching
     * @param fetchAssociations - Optional
     * @return - List of result DnaRun Entities.
     */
    @Override
    public List<DnaRun> getDnaRunsByDatasetId(Integer datasetId, Integer pageSize,
                                              Integer rowOffset, boolean fetchAssociations) {

        return getDnaRuns(pageSize, rowOffset,
                null, null,
                datasetId, null,
                null, null,
                null, null,
                fetchAssociations);

    }

    @Override
    public List<DnaRun> getDnaRuns(List<Integer> dnaRunIds, List<String> dnaRunNames,
                                   List<String> datasetIds) {

        List<DnaRun> dnaruns;

        List<Predicate> predicates = new ArrayList<>();

        String[] datasetIdsArray = new String[]{};

        try {

            CriteriaBuilder cb  = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<DnaRun> criteria = cb.createQuery(DnaRun.class);

            //Set Root entity and selected entities
            Root<DnaRun> root = criteria.from(DnaRun.class);

            criteria.select(root);

            if(dnaRunIds != null && dnaRunIds.size() > 0) {
                predicates.add(root.get("dnaRunId").in(dnaRunIds));
            }

            if(dnaRunNames != null && dnaRunNames.size() > 0) {
                predicates.add(root.get("dnaRunName").in(dnaRunNames));
            }

            /**Fetch markers only if the dnaRunIds or markerNames are not empty*/
            if(predicates.size() == 0) {
                String errorMsg = "All predicates are null. " +
                        "Either dnaRunIds or dnaRunNames are required";
                LOGGER.error(errorMsg);
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.UNKNOWN,
                        errorMsg);
            }

            if(!CollectionUtils.isEmpty(datasetIds)) {

                datasetIdsArray = datasetIds.toArray(new String[0]);

                ParameterExpression datasetIdsExp = cb.parameter(String[].class, "datasetIds");

                Expression<Boolean> datasetIdExists = cb.function(
                        "JSONB_EXISTS_ANY",
                        Boolean.class,
                        root.get("datasetMarkerIdx"),
                        datasetIdsExp
                );

                predicates.add(cb.isTrue(datasetIdExists));

            }

            criteria.where(predicates.toArray(new Predicate[]{}));
            criteria.orderBy(cb.asc(root.get("dnaRunId")));

            Query query =  em.createQuery(criteria);

            if(!CollectionUtils.isEmpty(datasetIds)) {
                query
                        .unwrap(org.hibernate.query.Query.class)
                        .setParameter("datasetIds", datasetIdsArray, StringArrayType.INSTANCE);
            }


            dnaruns = query.getResultList();

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
     * Lists DnaRun Entitis with given DnaRun Id
     * @param dnaRunIds - List of IDs of dnaRun.
     * @return - List of DnaRun Entity result
     */
    @Override
    public List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds) {

        return this.getDnaRuns(dnaRunIds, null, null);
    }


    /**
     * Lists DnaRun Entities with given DnaRun Names
     * @param dnaRunNames - List of Names for which dnaRuns need to be fecthed.
     * @return - List of DnaRun Entity result.
     */
    @Override
    public List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames) {
        return this.getDnaRuns(null, dnaRunNames, null);
    }

}
