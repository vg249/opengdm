package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.QueryParameterBean;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data access object Implementation for dataset Entity in the database
 *
 */
public class DatasetDaoImpl implements DatasetDao {

    Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

    @PersistenceContext
    protected EntityManager em;


    /**
     * Gets list of dataset entities that match the given filter parameters.
     * @param pageNum page number to be fetched. 0 based page index.
     * @param pageSize size of the page to be fetched.
     * @param datasetId to filter list of datasets by datasetId. When used return value should be list of size 1
     * @return List of dataset entity
     */
    @Override
    @Transactional
    public List<Dataset> listDatasetsByPageNum(Integer pageNum, Integer pageSize,
                                       Integer datasetId) {

        List<Dataset> datasets;

        final int defaultPageSize = 1000;
        final int defaultPageNum = 0;

        if (pageNum == null) pageNum = defaultPageNum;
        if (pageSize == null) pageSize = defaultPageSize;

        try {


            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> dataset = criteriaQuery.from(Dataset.class);

            criteriaQuery.select(dataset);

            if(datasetId != null) {
                criteriaQuery.where(criteriaBuilder.equal(dataset.get("datasetId"), datasetId));
            }

            Query listQuery = em.createQuery(criteriaQuery);

            listQuery
                    .setMaxResults(pageSize)
                    .setFirstResult(pageNum*pageSize);

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

    /**
     * Gets list of dataset entities that match the given filter parameters.
     * @param pageCursor page cursor used to fetch data.
     * @param pageSize size of the page to be fetched.
     * @return List of dataset entity
     */
    @Override
    @Transactional
    public List<Dataset> listDatasetsByPageCursor(String pageCursor, Integer pageSize) {

        List<Dataset> datasets;

        final int defaultPageSize = 1000;

        if (pageSize == null) pageSize = defaultPageSize;

        Integer datasetId;

        try {
            datasetId = Integer.parseInt(pageCursor);
        }
        catch(Exception e) {
            //Invalid page cursor return first page
            datasetId = 0;
        }

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> datasetRoot = criteriaQuery.from(Dataset.class);

            criteriaQuery.select(datasetRoot);

            criteriaQuery.where(criteriaBuilder.gt(datasetRoot.get("datasetId"), datasetId));

            criteriaQuery.orderBy(criteriaBuilder.asc(datasetRoot.get("datasetId")));

            Query listQuery = em.createQuery(criteriaQuery);

            listQuery
                    .setMaxResults(pageSize);

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




    //public List<Object[]> listDatasetsWithMarkersAndSamplesCounts(String pageCursor,
    //                                                              Integer pageSize) {

    //    List<Object[]> datasetsWithMarkersAndSamplesCount = new ArrayList<>();

    //    HashMap<Integer, Dataset> datasetsMapById = new HashMap<>();


    //    Integer pageOffset = null;


    //    String queryString = "WITH ds AS (" +
    //            "SELECT * " +
    //            "FROM dataset " +
    //            "WHERE :datasetId IS NULL OR dataset_id = :datasetId " +
    //            "LIMIT :pageSize OFFSET :pageOffset) " +
    //            "SELECT ds.* , anas.*, " +
    //            "(SELECT COUNT(marker_id) " +
    //            "FROM marker WHERE dataset_marker_idx -> CAST(ds.dataset_id AS TEXT) IS NOT NULL) " +
    //            "AS marker_count, " +
    //            "(SELECT COUNT(dnarun_id) " +
    //            "FROM dnarun WHERE dataset_dnarun_idx -> CAST(ds.dataset_id AS TEXT) IS NOT NULL) " +
    //            "AS dnarun_count " +
    //            "FROM ds " +
    //            "LEFT JOIN analysis AS anas ON(anas.analysis_id = ANY(ds.analyses)) ";



    //    for(Object[] tuple : resultTuplesList) {

    //        Dataset dataset = (Dataset) tuple[0];

    //        if(dataset == null) {
    //            continue;
    //        }

    //        if(datasetsMapById.containsKey(dataset.getDatasetId())) {
    //            datasetsMapById.get(dataset.getDatasetId()).getMappedAnalyses().add((Analysis) tuple[1]);
    //        }
    //        else {
    //            dataset.getMappedAnalyses().add((Analysis) tuple[1]);
    //            dataset.getMappedAnalyses().add(dataset.getCallingAnalysis());
    //            datasetsMapById.put(dataset.getDatasetId(), dataset);
    //            Object[] datasetWithMarkerAndSampleCountTuple = {dataset, tuple[2], tuple[3]};
    //            datasetsWithMarkersAndSamplesCount.add(datasetWithMarkerAndSampleCountTuple);
    //        }
    //    }

    //    return datasetsWithMarkersAndSamplesCount;


    //}





    /**
     * Returns list of tuple with Dataset entities joined with respective analysis entities.
     * Tuple contains values in the same order as below,
     * (Dataset, markerCount, dnasampleCount)
     * @param datasetId - Id for dataset. Unique identifier.
     * @param pageNum - Page number
     * @param pageSize - size of the page
     * @return
     */
    @Override
    @Transactional
    public List<Object[]> listDatasetsWithMarkersAndSamplesCounts(Integer pageNum,
                                                                  Integer pageSize,
                                                                  Integer datasetId) {


        NativerQueryRunner nativerQueryRunner = new NativerQueryRunner(em);

        List<Object[]> datasetsWithMarkersAndSamplesCount = new ArrayList<>();


        List<QueryParameterBean> queryParameterBeanList = new ArrayList<>();
        HashMap<String, Class> entityMap = new HashMap<>();
        List<String> scalarFiledsAlaises = new ArrayList<>();

       // List<Object[]> resultTuplesList = new ArrayList<>();


        HashMap<Integer, Dataset> datasetsMapById = new HashMap<>();


        Integer pageOffset = null;

        if(pageNum != null && pageSize != null) {
            pageOffset = pageNum * pageSize;
        }

        String queryString = "WITH ds AS (" +
                "SELECT * " +
                "FROM dataset " +
                "WHERE :datasetId IS NULL OR dataset_id = :datasetId " +
                "LIMIT :pageSize OFFSET :pageOffset) " +
                "SELECT ds.* , anas.*, " +
                "(SELECT COUNT(marker_id) " +
                "FROM marker WHERE dataset_marker_idx -> CAST(ds.dataset_id AS TEXT) IS NOT NULL) " +
                "AS marker_count, " +
                "(SELECT COUNT(dnarun_id) " +
                "FROM dnarun WHERE dataset_dnarun_idx -> CAST(ds.dataset_id AS TEXT) IS NOT NULL) " +
                "AS dnarun_count " +
                "FROM ds " +
                "LEFT JOIN analysis AS anas ON(anas.analysis_id = ANY(ds.analyses)) ";

        // Add query parameters
        queryParameterBeanList.add(new QueryParameterBean("pageSize", pageSize));
        queryParameterBeanList.add(new QueryParameterBean("pageOffset", pageOffset));
        queryParameterBeanList.add(new QueryParameterBean("datasetId", datasetId, IntegerType.INSTANCE));

        // Add entity map
        entityMap.put("ds", Dataset.class);
        entityMap.put("anas", Analysis.class);

        // Add scalar field alaises
        scalarFiledsAlaises.add("marker_count");
        scalarFiledsAlaises.add("dnarun_count");

        List<Object[]> resultTuplesList = nativerQueryRunner.run(
                queryString, queryParameterBeanList,
                entityMap, scalarFiledsAlaises);

        for(Object[] tuple : resultTuplesList) {

            Dataset dataset = (Dataset) tuple[0];

            if(dataset == null) {
                continue;
            }

            if(datasetsMapById.containsKey(dataset.getDatasetId())) {
                datasetsMapById.get(dataset.getDatasetId()).getMappedAnalyses().add((Analysis) tuple[1]);
            }
            else {
                dataset.getMappedAnalyses().add((Analysis) tuple[1]);
                dataset.getMappedAnalyses().add(dataset.getCallingAnalysis());
                datasetsMapById.put(dataset.getDatasetId(), dataset);
                Object[] datasetWithMarkerAndSampleCountTuple = {dataset, tuple[2], tuple[3]};
                datasetsWithMarkersAndSamplesCount.add(datasetWithMarkerAndSampleCountTuple);
            }
        }

        return datasetsWithMarkersAndSamplesCount;

    }


    @Override
    @Transactional
    public Dataset getDatasetById(Integer datasetId) {

        List<Dataset> datasetsById  = this.listDatasetsByPageNum(null, null, datasetId);

        if(datasetsById.size() > 1) {
            LOGGER.error("More than one duplicate entries found.");

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "More than one dataset entity exists for the same Id");

        }
        else if(datasetsById.size() == 0) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Dataset Entity for given id does not exist");
        }

        return datasetsById.get(0);

    }


}
