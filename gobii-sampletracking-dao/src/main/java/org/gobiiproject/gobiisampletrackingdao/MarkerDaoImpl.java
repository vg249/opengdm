package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

public class MarkerDaoImpl implements MarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;
    final int defaultPageNum = 0;


    @Override
    @Transactional
    public List<Marker> getMarkers(Integer pageNum, Integer pageSize,
                                   Integer markerId, Integer datasetId) {

        List<Marker> markers;


        try {

            //If either page size or page number is not provided, use default maximum limit which is 1000
            //and fetch the first page
            if(pageSize == null || pageNum == null) {
                pageNum = defaultPageNum;
                pageSize = defaultPageSize;
            }

            Session session = em.unwrap(Session.class);

            Criteria markerCriteria = session.createCriteria(Marker.class);


            if(markerId != null) {
                markerCriteria.add(Restrictions.eq("markerId", markerId));
            }

            if(datasetId != null) {
                markerCriteria.add(Restrictions.sqlRestriction(
                        "{alias}.dataset_marker_idx ?? ?", datasetId.toString(),
                        StringType.INSTANCE));
            }



            markerCriteria
                    .setMaxResults(pageSize)
                    .setFirstResult(pageNum*pageSize);

            markers = markerCriteria.list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return markers;

    }

    /**
     * Returns the object tuple of Marker Entity with Satrt and Stop from MarkerLinkageGroup Entity.
     * A given marker id may have more than one rows with different start stops
     * @param pageNum - Page Number to be fetched
     * @param pageSize -Page size to be fetched
     * @param markerId - Marker Id for which marker need to be fetched.
     * @param datasetId - To fetch the markers belonging to the dataset Id.
     * @return Tuple consists of below objects with respect to index,
     * 0 - Marker Entity
     * 1 - Start Position. Bigdecimal type.
     * 2 - Stop Position. Bigdecimal type.
     */
    @Override
    @Transactional
    public List<Object[]> getMarkerStartStopTuples(Integer pageNum, Integer pageSize,
                                                           Integer markerId, Integer datasetId) {


        List<Object[]> markerStartStops;

        try {

            //If either page size or page number is not provided, use default maximum limit which is 1000
            //and fetch the first page
            if(pageSize == null || pageNum == null) {
                pageNum = defaultPageNum;
                pageSize = defaultPageSize;
            }

            Session session = em.unwrap(Session.class);

            String queryString = "SELECT {marker.*}, markerli.start, markerli.stop from marker as marker " +
                    " LEFT JOIN marker_linkage_group AS markerli " +
                    " ON marker.marker_id = markerli.marker_id " +
                    " WHERE (marker.dataset_marker_idx -> :datasetId IS NOT NULL OR :datasetId = '') ";

            if(markerId != null) {

                queryString += " AND (marker.marker_id = :markerId) ";
            }


            Query markerStartStopsQuery = session.createNativeQuery(queryString);

            ((NativeQuery) markerStartStopsQuery)
                    .addEntity("marker", Marker.class)
                    .addScalar("start", BigDecimalType.INSTANCE)
                    .addScalar("stop", BigDecimalType.INSTANCE);

            String datasetIdParam = "";

            if(datasetId != null) {
               datasetIdParam = datasetId.toString();
            }
            markerStartStopsQuery.setParameter("datasetId", datasetIdParam);

            if(markerId != null) {
                markerStartStopsQuery.setParameter("markerId", markerId);
            }

            markerStartStopsQuery
                    .setMaxResults(pageSize)
                    .setFirstResult(pageNum*pageSize);

            markerStartStops = ((NativeQuery) markerStartStopsQuery).list();


        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return markerStartStops;

    }

}
