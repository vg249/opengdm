package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data access object Implementation for dnaSample Entity in the database
 *
 */
public class DnaSampleDaoImpl implements DnaSampleDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    private final Integer defaultPageSize = 1000;

    /**
     * Gets list of dnaSample entities that match the given filter parameters.
     * @param pageNum page number to be fetched. 0 based page index.
     * @param pageSize size of the page to be fetched.
     * @param projectId to filter dnaSamples belonging to given project id
     *                   Foreign key with ManyToOne relation(Many DnaSample to one germplasm).
     * @param dnaSampleId to filter dnaSamples by dnaSampleId. this filter should fetch only one value as it is primary key.
     * @param germplasmId to filter dnaSamples by germplasmId.
     *                   Foreign key with ManyToOne relation to Germplasm Entity(Many DnaSample to one germplasm).
     * @param germplasmExternalCode to filter dnasamples by germplasm external code.
     *
     * @return List of dnaSample entity
     */
    @Override
    @Transactional
    public List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize,
                                         Integer projectId, Integer dnaSampleId,
                                         Integer germplasmId, String germplasmExternalCode) {

        List<DnaSample> dnaSamples;

        try {


            Session session = em.unwrap(Session.class);

            Criteria dnaSampleCriteria = session.createCriteria(DnaSample.class);

            Criteria germplasmCriteria = dnaSampleCriteria.createCriteria("germplasm");

            if(dnaSampleId != null) {
                dnaSampleCriteria.add(Restrictions.eq("dnaSampleId", dnaSampleId));
            }

            if(projectId != null) {
                dnaSampleCriteria.add(Restrictions.eq("projectId", projectId));
            }

            if(germplasmId != null) {
                germplasmCriteria.add(Restrictions.eq("germplasmId", germplasmId));
            }


            if(germplasmExternalCode != null) {
                germplasmCriteria.add(Restrictions.eq("externalCode", germplasmExternalCode));
            }

            if(pageSize != null && pageNum != null) {
                dnaSampleCriteria
                        .setMaxResults(pageSize)
                        .setFirstResult(pageSize * pageNum);
            }
            else {
                //If either page size or page number is not provided, use default maximum limit which is 1000
                //and fetch the first page
                dnaSampleCriteria
                        .setMaxResults(defaultPageSize)
                        .setFirstResult(0);
            }

            dnaSamples = dnaSampleCriteria.list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return dnaSamples;

    }

    /**
     * Gets list of dnaSample entities by pageNum and pageSize.
     * @param pageNum page number to be fetched. required
     * @param pageSize size of the page to be fetched. required
     *
     * @return List of dnaSample entity
     */
    @Override
    @Transactional
    public List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize) {

        Objects.requireNonNull(pageNum);
        Objects.requireNonNull(pageSize);

        return getDnaSamples(pageNum, pageSize,
                null, null,
                null, null);

    }

    /**
     * Gets dnaSample entity by dnaSampleId.
     *
     * @return dnaSample entity for given dnaSampleId
     */
    @Override
    @Transactional
    public DnaSample getDnaSampleByDnaSampleId(Integer dnaSampleId) {

        List<DnaSample> dnaSamples = getDnaSamples(null, null,
                null, dnaSampleId,
                null, null);

        if(dnaSamples.size() > 1) {

            LOGGER.error("dnaSampleId has multiple entities association violating unique key constraint");

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNIQUE_KEY_VIOLATION,
                    "dnaSampleId has multiple entities association violating unique key constraint");
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
     * @param pageNum page number to be fetched.
     * @param pageSize size of the page to be fetched
     * @param projectId project id for which samples need to be fetched
     * @return List of dnaSample entity
     */
    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByProjectId(Integer pageNum, Integer pageSize, Integer projectId) {

        return getDnaSamples(pageNum, pageSize,
                projectId, null,
                null, null);

    }

    /**
     * Gets list of dnaSample entities that match the given filter criteria.
     * @param pageNum page number to be fetched.
     * @param pageSize size of the page to be fetched
     * @param germplasmId germplasm id for which samples need to be fetched
     * @return List of dnaSample entity
     */
    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByGermplasmId(Integer pageNum, Integer pageSize, Integer germplasmId) {

        return getDnaSamples(pageNum, pageSize,
                null, null,
                germplasmId,  null);

    }

    /**
     * Gets list of dnaSample entities that match the given filter criteria.
     * @param pageNum page number to be fetched.
     * @param pageSize size of the page to be fetched
     * @param germplasmExternalCode  germplasm external code for which samples need to be fetched
     * @return List of dnaSample entity
     */
    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByGermplasmExternalCode(Integer pageNum, Integer pageSize,
                                                                String germplasmExternalCode) {

        return getDnaSamples(pageNum, pageSize,
                null, null,
                null, germplasmExternalCode);

    }

}
