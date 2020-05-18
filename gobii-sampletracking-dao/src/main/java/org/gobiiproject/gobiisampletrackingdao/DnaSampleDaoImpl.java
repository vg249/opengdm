package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data access object Implementation for dnaSample Entity in the database.
 * Only gets paged results for result set of size more than one.
 * Default page size is 1000. Page Size and row offset can be set as function parameters
 *
 */
public class DnaSampleDaoImpl implements DnaSampleDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    /**
     * Gets list of dnaSample entities that match the given filter parameters.
     * @param pageSize size of the page to be fetched.
     * @param rowOffset Row Limit Offset.
     * @param projectId to filter dnaSamples belonging to given project id
     *                   Foreign key with ManyToOne
     *                   relation(Many DnaSample to one germplasm).
     * @param dnaSampleId to filter dnaSamples by dnaSampleId. this filter
     *                    should fetch only one value as it is primary key.
     * @param germplasmId to filter dnaSamples by germplasmId.
     *                   Foreign key with ManyToOne relation to
     *                    Germplasm Entity(Many DnaSample to one germplasm).
     * @param germplasmExternalCode to filter dnasamples by
     *                              germplasm external code.
     *
     * @return List of dnaSample entity
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public List<DnaSample> getDnaSamples(
        Integer pageSize, Integer rowOffset,
        Integer projectId, Integer dnaSampleId,
        Integer germplasmId, String germplasmExternalCode
    ) throws GobiiException {

        List<DnaSample> dnaSamples;

        List<Predicate> predicates = new ArrayList<>();

        try {

            Objects.requireNonNull("pageSize: Required not null");
            Objects.requireNonNull("rowOffset: Required not null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<DnaSample> criteriaQuery =
                criteriaBuilder.createQuery(DnaSample.class);

            Root<DnaSample> dnaSampleRoot = criteriaQuery.from(DnaSample.class);
            criteriaQuery.select(dnaSampleRoot);

            Join<Object, Object> germaplsmJoin =
                (Join<Object, Object>) dnaSampleRoot.fetch("germplasm");

            if(dnaSampleId != null) {
                predicates.add(
                    criteriaBuilder.equal(
                        dnaSampleRoot.get("dnaSampleId"), dnaSampleId));
            }

            if(projectId != null) {
                predicates.add(
                    criteriaBuilder.equal(
                        dnaSampleRoot.get("projectId"), dnaSampleId));
            }

            if(germplasmId != null) {
                predicates.add(
                    criteriaBuilder.equal(
                        germaplsmJoin.get("germplasmId"), germplasmId));
            }


            if(germplasmExternalCode != null) {
                predicates.add(
                    criteriaBuilder.equal(
                        germaplsmJoin.get("externalCode"),
                        germplasmExternalCode));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            dnaSamples = em.createQuery(criteriaQuery)
                .setMaxResults(pageSize)
                .setFirstResult(rowOffset)
                .getResultList();

            return dnaSamples;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() +
                        " Cause Message: " + e.getCause().getMessage());
        }
    }

    /**
     * Gets list of dnaSample entities by pageNum and pageSize.
     * @param rowOffset rowOffset for dnasample rows
     * @param pageSize size of the page to be fetched. required
     *
     * @return List of dnaSample entity
     */
    @Override
    public List<DnaSample> getDnaSamples(Integer pageSize, Integer rowOffset) {

        return getDnaSamples(pageSize, rowOffset,
                null, null,
                null, null);

    }

    /**
     * Gets dnaSample entity by dnaSampleId.
     *
     * @return dnaSample entity for given dnaSampleId or null
     */
    @Override
    public DnaSample getDnaSampleByDnaSampleId(Integer dnaSampleId)
        throws GobiiException {

        List<DnaSample> dnaSamples = getDnaSamples(2, 0,
                null, dnaSampleId,
                null, null);

        if(dnaSamples.size() > 1) {

            LOGGER.error("dnaSampleId has multiple entities " +
                "association violating unique key constraint");

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNIQUE_KEY_VIOLATION,
                    "dnaSampleId has multiple " +
                        "entities association violating unique key constraint");
        }
        else if(dnaSamples.size() == 1) {
            return dnaSamples.get(0);
        }
        else {
            return null;
        }

    }

    /**
     * Gets list of dnaSample entities that match the given filter criteria.
     * @param projectId project id for which samples need to be fetched
     * @param pageSize size of the page to be fetched
     * @param rowOffset - Offset of rows after which page should start.
     * @return List of dnaSample entity
     */
    @Override
    public List<DnaSample>
    getDnaSamplesByProjectId(
        Integer projectId, Integer pageSize, Integer rowOffset
    ) {
        return getDnaSamples(pageSize, rowOffset,
                projectId, null,
                null, null);
    }

    /**
     * Gets list of dnaSample entities that match the given filter criteria.
     * @param germplasmId germplasm id for which samples need to be fetched
     * @param pageSize size of the page to be fetched
     * @param rowOffset - Offset of rows after which page should start.
     * @return List of dnaSample entity
     */
    @Override
    public List<DnaSample> getDnaSamplesByGermplasmId(
        Integer germplasmId, Integer pageSize, Integer rowOffset
    ) {

        return getDnaSamples(pageSize, rowOffset,
                null, null,
                germplasmId,  null);

    }

    /**
     * Gets list of dnaSample entities that match the given filter criteria.
     * @param germplasmExternalCode  germplasm external code for which samples need to be fetched
     * @param pageSize size of the page to be fetched
     * @param rowOffset - Offset of rows after which page should start.
     * @return List of dnaSample entity
     */
    @Override
    public List<DnaSample>
    getDnaSamplesByGermplasmExternalCode(
        String germplasmExternalCode,
        Integer pageSize, Integer rowOffset
    ) {
        return getDnaSamples(pageSize, rowOffset,
                null, null,
                null, germplasmExternalCode);

    }

}
