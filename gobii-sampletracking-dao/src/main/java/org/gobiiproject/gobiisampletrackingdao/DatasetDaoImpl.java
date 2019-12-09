package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Data access object Implementation for dataset Entity in the database
 *
 */
public class DatasetDaoImpl implements DatasetDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;


    /**
     * Gets list of dnaSample entities that match the given filter parameters.
     * @param pageNum page number to be fetched. 0 based page index.
     * @param pageSize size of the page to be fetched.
     * @param datasetId to filter dnaSamples belonging to given project id
     *                   Foreign key with ManyToOne relation(Many DnaSample to one germplasm).
     * @return List of dataset entity
     */
    @Override
    @Transactional
    public List<Dataset> listDatasetsByPageNum(Integer pageNum, Integer pageSize,
                                       Integer datasetId) {

        List<Dataset> datasets;

        final int defaultPageSize = 1000;
        final int defaultPageNum = 0;

        try {


            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> dataset = criteriaQuery.from(Dataset.class);

            criteriaQuery.select(dataset);

            if(datasetId != null) {
                criteriaQuery.where(criteriaBuilder.equal(dataset.get("datasetId"), datasetId));
            }

            Query listQuery = em.createQuery(criteriaQuery);

            if(pageSize != null && pageNum != null) {
                    listQuery
                        .setMaxResults(pageSize)
                        .setFirstResult(pageSize * pageNum);
            }
            else {
                //If either page size or page number is not provided, use default maximum limit which is 1000
                //and fetch the first page
                listQuery
                        .setMaxResults(defaultPageSize)
                        .setFirstResult(defaultPageNum);
            }

            datasets = listQuery.getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return datasets;

    }
}
